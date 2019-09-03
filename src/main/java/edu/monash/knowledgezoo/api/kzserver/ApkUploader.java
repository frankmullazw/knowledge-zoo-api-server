package edu.monash.knowledgezoo.api.kzserver;

import edu.monash.knowledgezoo.api.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ApkUploader {
	/**
	 * upload apk to database. <br>
	 * if input is uri, will download the apk. <br>
	 * if input is apk, will convert apk to json metadata. <br>
	 * if input is json, will convert json to neo4j cypher. <br>
	 * finally execute cypher to add apk to database.
	 * @param approach "uri", "file", "json"
	 * @param payload all the content of the input is in the payload of http post method
	 * @throws IOException 
	 *
	 */
	public static void upload(String approach, InputStream payload, Neo4jDriver driver) throws IOException {
		InputStream apkFile = null;
		String jsonMetadata = null;
		switch (approach) {
			case "uri":
				String uri = Utils.getStringFrom(payload);
				apkFile = downloadApk(uri);
			case "file":
				if (apkFile == null)
					apkFile = payload;
				jsonMetadata = abstractMetadataFromApk(apkFile);
				apkFile.close();
			case "json":
			default:
				if (jsonMetadata == null)
					jsonMetadata = Utils.getStringFrom(payload);
				addMetadataToDatabase(jsonMetadata, driver);
		}
	}
	
	private static InputStream downloadApk(String uri) throws MalformedURLException, IOException {
		// open the stream and return it. the method caller should close the stream.
		return new URL(uri).openStream();
	}
	
	private static String abstractMetadataFromApk(InputStream apkFile) {
		// TODO abstract metadata from apk file
		// apkFile -> jsonMetadata
		String jsonMetadata = "";
		return jsonMetadata;
	}
	
	/**
	 * use cypher script to add abstracted apk metadata to database.
	 * the input json could contain more than one apk's info.
	 * each apk has multiple scripts to build itself in the database.
	 */
	private static void addMetadataToDatabase(String jsonMetadata, Neo4jDriver driver) {
		/*Map<String, List<String>> scriptsForApks = KnowledgeZooClient.convertJsonToScripts(jsonMetadata);
		try (Session session = driver.session()) {
			final int numApk = scriptsForApks.size();
			int num = 0;
			Log.d("num of apks: " + scriptsForApks.size());
			for (List<String> scripts : scriptsForApks.values()) {
				Log.d("num of scripts: " + scripts.size() + ", apk executed: " + (num++) + ", total apk: " + numApk);
				for (String script : scripts) {
					session.run(script);
				}
			}
		}
		Log.d("finish cypher");*/
	}
}
