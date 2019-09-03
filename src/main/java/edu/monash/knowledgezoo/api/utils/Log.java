package edu.monash.knowledgezoo.api.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
	// configuration of log
	private static final boolean sEnableLog = true;
	private static final LogLevel sEnabledLogLevel = LogLevel.D;
	private static final M[] sEnabledLogModule = new M[] {M.PrivateServlet, M.ApiServlet};
	private static final boolean sEnableLogToFile = false; // otherwise log into console
	private static final String sLogFilePath = "/";
	
	// enabled module bit map
	
	private static final int sEnabledLogModuleBit; // bit map represents which module is enabled
	
	static {
		// handle log modules
		int b = 0;
		for (M m : sEnabledLogModule) {
			b |= m.b;
		}
		sEnabledLogModuleBit = b;
		// handle logger
		Log log = new Log();
		sLogger = sEnableLogToFile ? log.new FileLogger() : log.new ConsoleLogger();
	}
	
	// log module
	public enum M {
		Null("", Integer.MAX_VALUE),
		PrivateServlet("privServlet", 0b00000001),
		ApiServlet("apiServlet", 0b00000010);;
		
		final String m;
		final int b;
		
		M(String m, int b) {
			this.m = m;
			this.b = b;
		}
		
		boolean isEnabled() {
			return (this.b & sEnabledLogModuleBit) != 0;
		}
	}
	
	private enum LogLevel {
		D, // debug
		I, // info
		W, // warning
		E; // error

		boolean isLowerThan(LogLevel l) {
			return this.ordinal() < l.ordinal();
		}
	}
	
	// logger
	
	private static final Logger sLogger;
	
	private abstract class Logger {
		public void log(LogLevel l, M m, String tag, String s) {
			if (!sEnableLog || l.isLowerThan(sEnabledLogLevel) || !m.isEnabled()) return;
			if (tag != null)
				s = tag + ": " + s;
			if (m != M.Null)
				s = m.m + " - " + s;
			log(l, s);
		}
		
		protected abstract void log(LogLevel l, String s);
	}
	
	/**
	 * log messages to console
	 */
	private class ConsoleLogger extends Logger {
		@Override
		public void log(LogLevel l, String s) {
			switch (l) {
				case D:
					System.out.println("DEBUG " + s);
					break;
				case I:
					System.out.println(s);
					break;
				case W:
					System.err.println("WARNNING " + s);
					break;
				case E:
					System.err.println(s);
					break;
			}
		}
	}
	
	/**
	 * log messages to file
	 * TODO: release file
	 */
	private class FileLogger extends Logger implements AutoCloseable {
		
		FileWriter writer;
		
		FileLogger() {
			try {
				writer = new FileWriter(sLogFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void log(LogLevel l, String s) {
			switch (l) {
				case D:
					s = "DEBUG " + s;
					break;
				case I:
					s = "INFO" + s;
					break;
				case W:
					s = "WARNNING " + s;
					break;
				case E:
					s = "ERROR" + s;
					break;
			}
			try {
				writer.write(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void close() throws Exception {
			if (writer != null)
				writer.close();
		}
	}
	
	// D

	public static void d(M m, String tag, String s) {
		sLogger.log(LogLevel.D, m, tag, s);
	}
	
	public static void d(M m, String s) {
		sLogger.log(LogLevel.D, m, null, s);
	}
	
	public static void d(String s) {
		sLogger.log(LogLevel.D, M.Null, null, s);
	}
	
	// I
	
	public static void i(M m, String tag, String s) {
		sLogger.log(LogLevel.I, m, tag, s);
	}
	
	public static void i(M m, String s) {
		sLogger.log(LogLevel.I, m, null, s);
	}
	
	public static void i(String s) {
		sLogger.log(LogLevel.I, M.Null, null, s);
	}
	
	// W
	
	public static void w(M m, String tag, String s) {
		sLogger.log(LogLevel.W, m, tag, s);
	}
	
	public static void w(M m, String s) {
		sLogger.log(LogLevel.W, m, null, s);
	}
	
	public static void w(String s) {
		sLogger.log(LogLevel.W, M.Null, null, s);
	}
	
	// E
	
	public static void e(M m, String tag, String s) {
		sLogger.log(LogLevel.E, m, tag, s);
	}
	
	public static void e(M m, String s) {
		sLogger.log(LogLevel.E, m, null, s);
	}
	
	public static void e(String s) {
		sLogger.log(LogLevel.E, M.Null, null, s);
	}
}
