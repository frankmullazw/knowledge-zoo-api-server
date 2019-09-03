package edu.monash.knowledgezoo.api.kzserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.monash.knowledgezoo.api.utils.Constants;
import edu.monash.knowledgezoo.api.utils.Log;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.AuthenticationException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * neo4j database driver wrapper
 * @author shin
 */
public class Neo4jDriver implements AutoCloseable, Constants {
	private Gson mGson = new GsonBuilder().create();
	private Driver mDriver; // neo4j sdk
	
	/**
	 * will build connection with database
	 */
	public Neo4jDriver() {
		try {
			mDriver = GraphDatabase.driver(sDatabaseUri, AuthTokens.basic(sDatabaseUser, sDatabasePwd));
			Log.i("connect to neo4j database, done!");
		} catch (ServiceUnavailableException e) {
			Log.e("cannot connect neo4j database!!!");
			e.printStackTrace();
		} catch (AuthenticationException e) {
			Log.e("neo4j database auth fail!!!");
			e.printStackTrace();
		}
	}
	
	private final StatementResult runAsResult(String cypher, Map<String, Object> params) {
		try (Session session = mDriver.session()) { // to let java invoke session.close() automatically
			StatementResult result = session.run(cypher, params);
			return result;
		}
	}
	
	private final StatementResult runAsResult(String cypher, Map<String, Object> params, Session session) {
		return session.run(cypher, params);
	}
	
	private final List<Map<String, Object>> resultToList(StatementResult result) {
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		while (result.hasNext())
			list.add(result.next().asMap());
		return list;
	}
	
	private final String listToJson(List<Map<String, Object>> list) {
		return mGson.toJson(list);
	}
	
	public String runAsJson(String cypher) {
		return runAsJson(cypher, null);
	}

	public String runAsJson(String cypher, Map<String, Object> params) {
		StatementResult result = runAsResult(cypher, params);
		List<Map<String, Object>> list = resultToList(result);
		return listToJson(list);
	}

	public List<Map<String, Object>> runAsList(String cypher) {
		return runAsList(cypher, null);
	}
	
	public List<Map<String, Object>> runAsList(String cypher, Map<String, Object> params) {
		StatementResult result = runAsResult(cypher, params);
		return resultToList(result);
	}
	
	public String runAsJson(String cypher, Map<String, Object> params, Session session) {
		StatementResult result = runAsResult(cypher, params, session);
		List<Map<String, Object>> list = resultToList(result);
		return listToJson(list);
	}

	public List<Map<String, Object>> runAsList(String cypher, Map<String, Object> params, Session session) {
		StatementResult result = runAsResult(cypher, params, session);
		return resultToList(result);
	}
	
	public String toJson(StatementResult result) {
		List<Map<String, Object>> list = resultToList(result);
		return listToJson(list);
	}
	
	public String toJson(Record record) {
		return mGson.toJson(record.asMap());
	}
	
	public String toJson(Object src) {
		return mGson.toJson(src);
	}
	
	/**
	 * more flexible way to operate neo4j database. should be familiar with neo4j API.
	 * @return
	 */
	public Session session() {
		return mDriver.session();
	}
	
	@Override
	public void close() {
		mDriver.close();
	}
}
