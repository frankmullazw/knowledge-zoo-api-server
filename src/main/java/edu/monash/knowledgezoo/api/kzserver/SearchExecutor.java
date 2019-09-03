package edu.monash.knowledgezoo.api.kzserver;

import org.neo4j.driver.v1.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SearchExecutor {
	/**all labels and its properties, key:label, val:properties*/
	private static Map<String, List<String>> mLabelProperties;
	
	/**
	 * search database according to query sentence from user
	 * @param query query sentence from user
	 * @return result as json
	 */
	public static String search(String query, Neo4jDriver driver) {
		// get labels and properties
		if (mLabelProperties == null)
			mLabelProperties = getLabelProperties(driver);
		Map<String, List<String>> properties = mLabelProperties;
		
		// prepare params map
		Map<String, Object> params = getParams(query);
		
		// start session
		Map<String, Map<String, List<Map<String, Object>>>> tables = new HashMap<>(); // key:label, val:(key:property, val:record list)
		try (Session session = driver.session()) {
			//for each property in all labels
			for (Entry<String, List<String>> entry : properties.entrySet()) {
				String label = entry.getKey();
				List<String> props = entry.getValue();
				// get apks whose property in label matches regExpr
				Map<String, List<Map<String, Object>>> propMap = new HashMap<>(); // key: property, val: record list(key: record key, val: record val)
				// run cypher to get apk infos
				for (String prop : props) {
					String cypher = getCypher(label, prop);
					List<Map<String, Object>> list = driver.runAsList(cypher, params, session);
					// if no apk info found, don't add this property
					if (!list.isEmpty())
						propMap.put(prop, list);
				}
				// if no apk info found under this lable, don't add this lable
				if (!propMap.isEmpty())
					tables.put(label, propMap);
			}
		}
		return driver.toJson(tables);
	}
	
	/**
	 * get all labels and its properties
	 * @return key:label, val:properties of this label
	 */
	private static Map<String, List<String>> getLabelProperties(Neo4jDriver driver) {
		Map<String, List<String>> properties = new HashMap<>(); // key:label, val:properties of this label
		//get all labels in this database
		List<Map<String, Object>> labels = driver.runAsList("CALL db.labels() YIELD label\n" + 
															"RETURN label");
		for (Map<String, Object> e : labels) {
			String label = (String) e.get("label");
			// get all properties under this label
			List<Map<String, Object>> propList = driver.runAsList("MATCH (n:" + label + ")\n" + 
																"WITH n LIMIT 1\n" +
																"RETURN keys(n)");
			@SuppressWarnings("unchecked")
			List<String> props = (List<String>) propList.get(0).get("keys(n)");
			// add properties of this label to the map
			properties.put(label, props);
		}
		return properties;
	}
	
	/**
	 * get params to pass with cypher
	 * @param query user input query
	 * @return key: param name, val:param value
	 */
	private static Map<String, Object> getParams(String query) {
		String[] words = query.split(" ");
		// regular expression
		String regExpr = null;
		for (String w : words) {
			if (regExpr == null) {
				regExpr = w; // regExpr is the first word
				continue;
			}
			regExpr += ("|" + w);
		}
		regExpr = "^.*(" + regExpr + ").*$";
		// prepare params map
		Map<String, Object> params = new HashMap<>();
		params.put("regExpr", regExpr);
		return params;
	}
	
	private static String getCypher(String label, String prop) {
		String cypher;
		switch (label) {
			case "APK":
				cypher = "MATCH (apk:APK)\n" + 
						"WHERE exists(apk." + prop + ") AND apk." + prop + " =~ $regExpr\n" + 
						"RETURN DISTINCT apk.name AS sha256, apk.packageName AS pkgName";
				break;
			case "Action":
			case "Category":
				cypher = "MATCH (apk:APK) - [:Has] - (c:COMP) - [] -> (n:"+ label +")\n" + 
						"USING INDEX n:" + label + "(" + prop + ")\n" + 
						"WHERE exists(n." + prop + ") AND n." + prop + " =~ $regExpr\n" + 
						"RETURN DISTINCT apk.name AS sha256, apk.packageName AS pkgName, c AS comp, n." + prop + " AS " + label + prop;
				break;
			default:
				cypher = "MATCH (apk:APK) - [] -> (n:"+ label +")\n" + 
						"USING INDEX n:" + label + "(" + prop + ")\n" + 
						"WHERE exists(n." + prop + ") AND n." + prop + " =~ $regExpr\n" + 
						"RETURN DISTINCT apk.name AS sha256, apk.packageName AS pkgName, n." + prop + " AS " + label + prop;
		}
		return cypher;
	}
}
