package edu.monash.knowledgezoo.api.kzservlet;

import edu.monash.knowledgezoo.api.kzserver.HistoryHandler;
import edu.monash.knowledgezoo.api.kzserver.Neo4jDriver;
import edu.monash.knowledgezoo.api.kzserver.StatisticsHandler;
import edu.monash.knowledgezoo.api.utils.Constants;
import edu.monash.knowledgezoo.api.utils.Log;
import edu.monash.knowledgezoo.api.utils.Log.M;
import edu.monash.knowledgezoo.api.utils.Looper;
import edu.monash.knowledgezoo.api.utils.Looper.Dispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/KZPrivateServlet")
public class KZPrivateServlet extends HttpServlet implements Dispatcher, Constants {
	private static final long serialVersionUID = 2L;
	
	private Neo4jDriver mDriver; // neo4j sdk
	private Looper mLooper;
	
	private StatisticsHandler mStatistics;
	private HistoryHandler mHistory;
	
    @Override
	public void init() {
    	// init all components
    	mDriver = new Neo4jDriver();
    	
    	mLooper = new Looper();
    	mLooper.setDispatcher(this);
    	mLooper.start();
    	
    	mStatistics = new StatisticsHandler();
    	mHistory = new HistoryHandler(Constants.sMaxHistoryDisplayed); // for read to servlet
    	
    	// setup delayed tasks to update statistics and histories automatically
    	setupTasks();
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
    	Log.i(M.PrivateServlet, "doGet", requestUri);
    	
    	String result = null;
    	String[] paths = requestUri.split("/");
    	switch (paths[0]) {
	    	case "statistics": // private/statistics/*
	    		if (paths.length <= 1 || paths[1].equals("")) // no specific statistic required
	    			result = mStatistics.getSummary();
	    		else { // require a specific statistic
	    			if (paths[1].equals("lib-lineage")) // private/statistics/lib-lineage?pkgName=xxx&lib=xxx
	    				result = mStatistics.getLibLineageStatistic(mDriver, request.getParameter("pkgName"), request.getParameter("lib"));
	    			else
	    				result = mStatistics.getStatistics(paths[1]);
	    		}
	    		break;
	    	case "history": // private/history
	    		result = mHistory.getRecentHistory();
	    		break;
    	}
    	// write result to user
		if (result != null) {
			response.getWriter().write(result);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
	}

    /**
     * setup delayed tasks to update statistics and histories automatically
     */
    private void setupTasks() {
    	// generate first statistics and history right now
    	mLooper.post(CODE_UPDATE_STATISTICS);
    	mLooper.post(CODE_UPDATE_HISTORY);
    	// update statistics and history later repeatedly
    	mLooper.post(CODE_UPDATE_STATISTICS, null, PEROID_UPDATE_STATISTICS, true);
    	mLooper.post(() -> mLooper.post(CODE_UPDATE_HISTORY, null, PEROID_UPDATE_HISTORY, true), PEROID_UPDATE_HISTORY / 4); // run this later
    }
    
	@Override
	public void dispatch(int code, Object param) {
		Log.i(M.PrivateServlet, "dispatch", "code = " + code);
		switch (code) {
			case CODE_UPDATE_STATISTICS:
				mStatistics.update(mDriver);
				break;
			case CODE_UPDATE_HISTORY:
				mHistory.update();
				break;
		}
	}
}
