package edu.monash.knowledgezoo.api.kzserver;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Save every search history and provide recent histories for display.</br>
 * Read & write history data is from different process (two servlets), and the request
 * of read & write are both very frequent and concurrent.</br>
 * One way of handling frequent concurrent IPC request is to use relational database/socket/file.</br>
 * The display of history doesn't need to be on time, which gives us an opportunity
 * to optimize I/O performance.</br>
 * In order to avoid too frequent I/O operation, use memory to cache some history records for write,
 * and write them together to database in a specific time point. read as well.</br>
 * 
 * @author shin
 */
public class HistoryHandler {
	/*************** for write ****************/
	
	private List<HistoryElement> mRecentHistoryList;
	private String mHistoryFilePath;
	
	public HistoryHandler(String historyFilePath) {
		mRecentHistoryList = new ArrayList<>();
		mHistoryFilePath = historyFilePath;
	}
	
	public synchronized void addHistory(String query) {
		// add to list in memory
		mRecentHistoryList.add(new HistoryElement(System.currentTimeMillis(), query));
	}
	
	public void save() throws IOException {
		List<HistoryElement> histories = mRecentHistoryList;
		if (histories == null) return;
		// create new history list to avoid concurrent problems and ignore GC impact
		synchronized (this) {
			mRecentHistoryList = new ArrayList<>();
		}
		// save in-memory histories to file for recording purpose
		try (FileWriter writer = new FileWriter(mHistoryFilePath, true)) {
			for (HistoryElement e : histories)
				writer.write(e.toString() + "\n");
		}
		
		// TODO: send in-memory histories to the reader for display
		sHistoryQueueIPC.addAll(histories);
	}
	
	/*************** for read ****************/
	
	private int mNumberOfHistoryInMemory;
	private String mRecentHistoryJson;
	
	/**
	 * @param numOfRecentHistory number of recent history records for display
	 */
	public HistoryHandler(int numOfRecentHistory) {
		mNumberOfHistoryInMemory = numOfRecentHistory;
		mRecentHistoryJson = "[]"; // empty
	}
	
	public synchronized String getRecentHistory() {
		return mRecentHistoryJson;
	}
	
	public void update() {
		// limit the amount of histories to be displayed
		while (sHistoryQueueIPC.size() > mNumberOfHistoryInMemory)
			sHistoryQueueIPC.remove();
		// TODO: read recent history from IPC
		Gson gson = new Gson();
		synchronized (this) {
			// convert histories to json format
			mRecentHistoryJson = gson.toJson(sHistoryQueueIPC);
		}
	}
	
	/*************** for read & write ****************/
	
	// TODO: temporary in-process solution to mock up an IPC mechanism
	private static ConcurrentLinkedQueue<HistoryElement> sHistoryQueueIPC = new ConcurrentLinkedQueue<>();
	
	public synchronized void close() {
		// write the rest histories to file
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// close database/socket/file handler
	}

	private class HistoryElement {
		final long time;
		final String query;
		
		private HistoryElement(long time, String query) {
			this.time = time;
			this.query = query;
		}
		
		@Override
		public String toString() {
			Date date = new Date(time);
			SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
			return time + "\t" + format.format(date) + "\t" + query;
		}
	}
}
