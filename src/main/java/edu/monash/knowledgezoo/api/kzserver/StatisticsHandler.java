package edu.monash.knowledgezoo.api.kzserver;

import edu.monash.knowledgezoo.api.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatisticsHandler {
	private String mStatisticsSummary;
	private Map<String, String> mStatisticsMap;
	
	public StatisticsHandler() {
		mStatisticsMap = new HashMap<>();
	}
	
	/**
	 * get a summary of all statistics for display.
	 * this summary only contains part of each statistic, to reduce json length.
	 * @return json format
	 */
	public synchronized String getSummary() {
		return mStatisticsSummary;
	}
	
	/**
	 * get a particular statistics by its name
	 * @return json format. null if this statistic doesn't exist.
	 */
	public synchronized String getStatistics(String statisticName) {
		return mStatisticsMap.get(statisticName);
	}
	
	/**
	 * generate all statistics and update the current ones.
	 */
	public void update(Neo4jDriver driver) {
		String statSummary = null;
		Map<String, String> statMap = new HashMap<>();
		
		// k: statistic name, v: list of records {k: record key, v: record value}
		Map<String, List<Map<String, Object>>> stats = new HashMap<>();
		generateStatistic(driver, stats);
		
		// generate statistics map and summary
		for (Entry<String, List<Map<String, Object>>> e: stats.entrySet()) {
			String statName = e.getKey();
			List<Map<String, Object>> statList = e.getValue();
			// generate statistics map
			String statJson = driver.toJson(statList);
			statMap.put(statName, statJson);
			// generate statistics summary
			if (statList.size() > Constants.sMaxStatInSummary) {
				List<Map<String, Object>> statSubList = statList.subList(0, Constants.sMaxStatInSummary - 1);
				stats.put(statName, statSubList);
			}
		}
		statSummary = driver.toJson(stats);
		// lock when update field parameter to avoid read and write them at the same time
		synchronized (this) {
			mStatisticsSummary = statSummary;
			mStatisticsMap = statMap;
		}
	}
	
	private void generateStatistic(Neo4jDriver driver, Map<String, List<Map<String, Object>>> statistics) {
		// top permission name
		String cypher = getCypher("top-permissions", "PERM", "name");
		statistics.put("top-permissions", driver.runAsList(cypher));
		// top package name
		cypher = getCypher("top-packages", "PKG", "name");
		statistics.put("top-packages", driver.runAsList(cypher));
		// top library (not app package)
		cypher = getCypher("top-libraries");
		statistics.put("top-libraries", driver.runAsList(cypher));
	}
	
	/**
	 * 
	 * @param statName
	 * @param params in neo4j cypher, label and property have to be hard coded into cypher query, instead of using parameter map.
	 */
	private String getCypher(String statName, String... params) {
		String r = "";
		switch (statName) {
			case "top-permissions":
			case "top-packages":
				r = "MATCH (:APK) - [] -> (l:" + params[0] + ")\n" + 
					"WITH l." + params[1] + " AS prop\n" +
					"WITH collect(prop) AS rows, prop\n" + 
					"RETURN size([r IN rows WHERE r = prop]) AS num, prop AS " + params[1] + "\n" + 
					"ORDER BY num DESC\n" + 
					"LIMIT " + Constants.sTopStatNum;
				break;
			case "top-libraries":
				r = "MATCH (apk:APK) - [:Has] -> (pkg:PKG)\n" + 
					"WHERE NOT pkg.name STARTS WITH apk.packageName\n" + 
					"WITH pkg.name AS pn\n" + 
					"WITH collect(pn) AS rows, pn\n" + 
					"RETURN size([r IN rows WHERE r = pn]) AS num, pn AS pkgName\n" + 
					"ORDER BY num DESC\n" + 
					"LIMIT " + Constants.sTopStatNum;
				break;
			case "lib-lineage":
				r = "MATCH (pkg:PKG) <- [:Has] - (apk:APK) - [:SignedBy] -> (cert:CERT)\n" + 
					"WHERE apk.packageName =~ $pkgName AND pkg.name =~ $lib AND NOT pkg.name STARTS WITH apk.packageName\n" + 
					"RETURN apk.versionCode AS versionCode, apk.versionName AS versionName, cert.fingerprint AS signiture, apk.packageName AS apkName, pkg.name AS libName\n" + 
					"ORDER BY versionCode";
				break;
		}
		return r;
	}
	
	public String getLibLineageStatistic(Neo4jDriver driver, String packageName, String lib) {
		// TODO temporary function until requirement is clear
		String cypher = getCypher("lib-lineage");
		Map<String, Object> param = new HashMap<>();
		param.put("pkgName", "^.*(" + packageName + ").*$");
		param.put("lib", "^.*(" + lib + ").*$");
		
		Map<String, List<Map<String, Object>>> result = new HashMap<>();
		result.put("lib-lineage", driver.runAsList(cypher, param));
		return driver.toJson(result);
	}
}
