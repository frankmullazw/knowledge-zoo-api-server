package edu.monash.knowledgezoo.api.kzservlet;

import edu.monash.knowledgezoo.api.kzserver.*;
import edu.monash.knowledgezoo.api.kzserver.ConfiguredMethods.KZRestfulApi;
import edu.monash.knowledgezoo.api.utils.Constants;
import edu.monash.knowledgezoo.api.utils.Log;
import edu.monash.knowledgezoo.api.utils.Log.M;
import edu.monash.knowledgezoo.api.utils.Looper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/KZApiServlet")
public class KZApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Neo4jDriver mDriver; // neo4j sdk
	private ConfiguredMethods mMethods;
	
	private Looper mLooper;
	private HistoryHandler mHistory;
    
    @Override
	public void init() {
    	mMethods = new ConfiguredMethods(getServletContext().getRealPath(Constants.sConfigXmlFilePath));
    	mDriver = new Neo4jDriver();
    	
    	mLooper = new Looper();
    	mLooper.start();
    	
    	mHistory = new HistoryHandler(getServletContext().getRealPath(Constants.sHistoryFileLocation));
    	
    	// save recent histories in memory repeatedly
    	mLooper.post(() -> {
			try {
				mHistory.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, Constants.PEROID_SAVE_HISTORY, true);
    }

    @Override
	public void destroy() {
    	mHistory.close();
    	mLooper.stop();
		mDriver.close();
	}
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String requestUri = request.getPathInfo().substring(1); // delete the beginning slash
    	Log.i(M.ApiServlet, "doGet", requestUri);
    	
    	String[] paths = requestUri.split("/");
    	switch (paths[0]) {
	    	case "search": // api/search?q=xxx
	    		search(request, response);
	    		break;
	    	default: // configured method
	    		executeConfiguredMethod(request, response);
    	}
	}
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestUri = request.getPathInfo().substring(1); // delete the beginning slash
		Log.i(M.ApiServlet, "doPost", requestUri);
		
		String[] paths = requestUri.split("/");
		switch (paths[0]) {
			case "apk": // POST /apk?approach={uri, file, json}
				uploadApk(request, response);
				break;
		}
	}
    
    private void executeConfiguredMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String requestApiName = request.getPathInfo().substring(1); // delete the beginning slash
		Log.i(M.ApiServlet, "configured method", requestApiName);
		// search requested api in configuration
		KZRestfulApi api = mMethods.getMethod(requestApiName);
		if (api == null) {
			response.getWriter().append("requested api doesn't exit");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// add request params to configuration
		Map<String, Object> params = new HashMap<>();
		params.putAll(api.inputs);
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String requestParam = e.nextElement();
			params.put(requestParam, request.getParameter(requestParam));
		}
		// operate api.script to graph database and return api.output to response
		String resultJson = mDriver.runAsJson(api.script, params);
		response.getWriter().append(resultJson);
		response.setStatus(HttpServletResponse.SC_OK);
    }
	
	private void uploadApk(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try (InputStream payload = request.getInputStream()) {
			ApkUploader.upload(request.getParameter("approach"), payload, mDriver);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(M.ApiServlet, "upload apk", "failt to upload apk");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	private void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Log.i(M.ApiServlet, "search", request.getQueryString());
		String query = request.getParameter("q"); // GET /search?q=xxxx
		if (query == null) {
			response.getWriter().write("unkown query format");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		else if (!query.isEmpty()) {
			Log.d(M.ApiServlet, "search", "start search");
			String resultJson = SearchExecutor.search(query, mDriver);
			Log.d(M.ApiServlet, "search", "finish search");
			// save query
			mHistory.addHistory(query);
			// print results
			response.getWriter().write(resultJson);
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}
}
