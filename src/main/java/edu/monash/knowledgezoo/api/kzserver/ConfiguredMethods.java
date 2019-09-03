package edu.monash.knowledgezoo.api.kzserver;

import edu.monash.knowledgezoo.api.utils.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguredMethods {
	private volatile List<KZRestfulApi> mConfiguration; // stores config from xml
	
	/**
	 * will load configuration from file
	 */
	public ConfiguredMethods(String configureFilePath) {
		try {
			updateConfiguration(configureFilePath);
			Log.i("load configuration file successfully");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("load configuration file fail!!!");
		}
	}
	
	/**
	 * load/reload configuration file from disk. It's safe to reload by call this function directly.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public synchronized void updateConfiguration(String configureFilePath) throws ParserConfigurationException, SAXException, IOException {
		// no need to consider concurrent problem when update configuration xml file.
		// if the last mConfiguration instance was being read when we change its reference to
		// a new instance, the last operation will continue to use the old instance.
		mConfiguration = parseXmlConfigFile(configureFilePath);
	}
	
	/**
	 * @return null if not found
	 */
	public synchronized KZRestfulApi getMethod(String name) {
		for (KZRestfulApi api : mConfiguration)
			if (api.name.equals(name))
				return api;
		return null;
	}
	
	/**
	 * represents one restful api defined in configuration file
	 * @author shin
	 */
	public class KZRestfulApi {
		/** name of the restful API */
		public String name;
		/** params of the API from client */
		public Map<String, Object> inputs;
		/** returned output to client */
		public String output;
		/** cypher script to operate graph database */
		public String script;
	}
	
	/**
	 * load config xml file from disk. will create the configuration.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private List<KZRestfulApi> parseXmlConfigFile(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File(xmlFilePath);
		// build DOM tree from file
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        // create KZRestfulApi list based on api nodes
        List<KZRestfulApi> kzApiList = new ArrayList<KZRestfulApi>();
        // parse nodes from DOM
        NodeList nodeApiList = doc.getElementsByTagName("api");
        for (int i = 0; i < nodeApiList.getLength(); i++) {
        	Element nodeApi = (Element) nodeApiList.item(i); // api node
        	KZRestfulApi kzApi = new KZRestfulApi();
        	// name node of this api
        	Node nodeName = nodeApi.getElementsByTagName("name").item(0);
        	if (nodeName != null)
        		kzApi.name = nodeName.getTextContent().trim();
        	// output node of this api
        	Node nodeOutput = nodeApi.getElementsByTagName("output").item(0);
        	if (nodeOutput != null)
        		kzApi.output = nodeOutput.getTextContent().trim();
        	// script node of this api
        	Node nodeScript = nodeApi.getElementsByTagName("script").item(0);
        	if (nodeScript != null)
        		kzApi.script = nodeScript.getTextContent().trim();
        	// inputs node of this api
        	kzApi.inputs = new HashMap<>();
        	NodeList nodeInputsList = nodeApi.getElementsByTagName("input");
        	for (int j = 0; j < nodeInputsList.getLength(); j++) {
				Element nodeInput = (Element) nodeInputsList.item(j);
				kzApi.inputs.put(nodeInput.getAttribute("key"), nodeInput.getTextContent().trim());
			}
        	// add kzApi to api list
        	kzApiList.add(kzApi);
        }
        return kzApiList;
	}
}
