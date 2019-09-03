package edu.monash.knowledgezoo.api.utils;

public interface Constants {
	/** configuration file path corresponding to the structure of .war file*/
	String sConfigXmlFilePath = "/WEB-INF/res/kzserver_config.xml";
	
	// neo4j database
	String sDatabaseUri = "bolt://localhost:7687";
	String sDatabaseUser = "neo4j";
	String sDatabasePwd = "123456";
	
	// for repeated tasks, in milli second
	int PEROID_UPDATE_STATISTICS = 10 * 60 * 1000; // 10 minutes
	int PEROID_UPDATE_HISTORY = PEROID_UPDATE_STATISTICS;
	int PEROID_SAVE_HISTORY = PEROID_UPDATE_STATISTICS;
	
	// for dispatcher
	int CODE_UPDATE_STATISTICS = 1;
	int CODE_UPDATE_HISTORY = 2;
	
	// for statistics function
	int sMaxStatInSummary = 10;
	int sTopStatNum = 10;
	
	// for history function
	String sHistoryFileLocation = "/data/search_history.txt";
	int sMaxHistoryDisplayed = 20;
}
