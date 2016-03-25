package com.bitdagger.dungeon.logging;

import java.io.PrintStream;

public final class Logger
{
	/**
	 * Singleton instance
	 */
	private static Logger instance;
	
	/**
	 * Output stream
	 */
	private PrintStream out;
	
	/**
	 * Error stream
	 */
	private PrintStream err;
	
	/**
	 * Enable debugging
	 */
	private boolean debugging;
	
	/**
	 * Construct a new Logger object
	 */
	private Logger()
	{
		// Default to system out and err with no debugging
		this.out = System.out;
		this.err = System.err;
		this.debugging = false;
	}
	
	/**
	 * Singleton accessor
	 * 
	 * @return Singleton instance
	 */
	public static Logger instance()
	{
		if (Logger.instance == null) {
			Logger.instance = new Logger();
		}
		
		return Logger.instance;
	}
	
	/**
	 * Enable debugging
	 */
	public void enableDebugging()
	{
		this.debugging = true;
	}
	
	/**
	 * Disable debugging
	 */
	public void disableDebugging()
	{
		this.debugging = false;
	}
	
	/**
	 * Output stream accessor
	 * 
	 * @return The current output stream
	 */
	public PrintStream getOutputStream()
	{
		return this.out;
	}
	
	/**
	 * Error stream accessor
	 * 
	 * @return The current error stream
	 */
	public PrintStream getErrorStream()
	{
		return this.err;
	}
	
	/**
	 * Set a new output stream
	 * 
	 * @param out New output stream to use
	 * 
	 * @return The previous output stream
	 */
	public PrintStream setOutputStream(PrintStream out)
	{
		PrintStream old = this.out;
		this.out = out;
		
		return old;
	}
	
	/**
	 * Set a new error stream
	 * 
	 * @param out New error stream to use
	 * 
	 * @return The previous error stream
	 */
	public PrintStream setErrorStream(PrintStream err)
	{
		PrintStream old = this.err;
		this.err = err;
		
		return old;
	}
	
	/**
	 * Print the message directly to the output stream
	 * 
	 * @param msg Message to log
	 */
	public void raw(String msg)
	{
		this.raw(msg, false);
	}
	
	/**
	 * Print the message directly to the output stream with an optional newline
	 * 
	 * @param msg Message to log
	 * @param newline Should a newline be appended to the message
	 */
	public void raw(String msg, boolean newline)
	{
		if (newline) {
			this.out.println(msg);
		} else {
			this.out.print(msg);
		}
	}
	
	/**
	 * Log the message to the appropriate output stream
	 * 
	 * @param lvl Log level for the message
	 * @param msg Message to log
	 */
	public void log(LogLevel lvl, String msg)
	{
		PrintStream log;
		
		switch(lvl)
		{
			case DEBUG:
				if (!this.debugging) {
					return;
				}
				log = this.out;
				break;
			case NOTICE:
			case WARN:
			case ERROR:
			case FATAL:
				log = this.err;
				break;
			default:
				log = this.out;
		}
		
		log.println(String.format("[%s] %s", lvl.getValue(), msg));
	}
	
	/**
	 * Convenience function for debug messages
	 * 
	 * @param msg Message to log
	 */
	public void debug(String msg)
	{
		if (!this.debugging) {
			return;
		}
		
		this.log(LogLevel.DEBUG, msg);
	}
	
	/**
	 * Convenience function for info messages
	 * 
	 * @param msg Message to log
	 */
	public void info(String msg)
	{
		this.log(LogLevel.INFO, msg);
	}
	
	/**
	 * Convenience function for notice messages
	 * 
	 * @param msg Message to log
	 */
	public void notice(String msg)
	{
		this.log(LogLevel.NOTICE, msg);
	}
	
	/**
	 * Convenience function for warning messages
	 * 
	 * @param msg Message to log
	 */
	public void warn(String msg)
	{
		this.log(LogLevel.WARN, msg);
	}
	
	/**
	 * Convenience function for error messages
	 * 
	 * @param msg Message to log
	 */
	public void error(String msg)
	{
		this.log(LogLevel.ERROR, msg);
	}
	
	/**
	 * Convenience function for fatal error messages
	 * 
	 * @param msg Message to log
	 */
	public void fatal(String msg)
	{
		this.log(LogLevel.FATAL, msg);
	}
}
