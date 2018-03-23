/*
 * C-Hack Panel Java Control System
 * 
 * @author Tim Neumann
 * @version 1.0.0
 * @copyright (c) Tim Neumann 2017-2018
 * @license:
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */
package de.c_hack.tim.cHackPanelControl;

import de.c_hack.tim.lib.Log;

/**
 * The abstarct class which all animation snippets/programs need to extend.
 * 
 * @author Tim Neumann
 */
public abstract class AnimationSnippet {

	/**
	 * Logs message as Info
	 * 
	 * @param message
	 *            The message to log.
	 */
	public static void log(String message) {
		Main.getLog().log(message, Log.INFO);
	}

	/**
	 * Logs message as Debug
	 * 
	 * @param message
	 *            The message to log.
	 */
	public static void debug(String message) {
		Main.getLog().log(message, Log.DEBUG);
	}

	/**
	 * Logs message as Warn
	 * 
	 * @param message
	 *            The message to log.
	 */
	public static void warn(String message) {
		Main.getLog().log(message, Log.WARN);
	}

	/**
	 * Logs message as Error
	 * 
	 * @param message
	 *            The message to log.
	 */
	public static void error(String message) {
		Main.getLog().log(message, Log.ERROR);
	}

	/**
	 * Get's the log instance
	 * 
	 * @return The main log instance.
	 */
	public static Log getLog() {
		return Main.getLog();
	}

	/**
	 * The times per second {@link #loop() loop()} get's run.
	 */
	private float freq = 20;

	/**
	 * Get's run once at the beginning
	 * 
	 * @param args
	 *            The passed command line arguments.(Containing the name of this
	 *            executed class as the first one).
	 */
	public abstract void setup(String[] args);

	/**
	 * Get's run {@link #freq freq} times a second
	 */
	public abstract void loop();

	/**
	 * Sleeps for t milliseconds
	 * 
	 * @param t
	 *            The milliseconds to sleep
	 */
	@SuppressWarnings("static-method")
	public void sleepMillis(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			Main.getLog().logException(e, Log.WARN, false);
		}
	}

	/**
	 * Sleeps for t seconds
	 * 
	 * @param t
	 *            The seconds to sleep
	 */
	public void sleep(int t) {
		sleepMillis(1000 * t);
	}

	/**
	 * Get's {@link #freq freq}
	 * 
	 * @return freq
	 */
	public float getFreq() {
		return this.freq;
	}

	/**
	 * Set's {@link #freq freq}
	 * 
	 * @param par_freq
	 *            freq
	 */
	public void setFreq(float par_freq) {
		this.freq = par_freq;
	}
}
