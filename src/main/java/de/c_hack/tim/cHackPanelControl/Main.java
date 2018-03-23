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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import de.c_hack.tim.cHackPanelControl.animationSnippets.GenericSnippet;
import de.c_hack.tim.lib.Config;
import de.c_hack.tim.lib.Log;

/**
 * The main class managing all the control flow.
 * 
 * @author Tim Neumann
 */
public class Main {
	/**
	 * The config instance
	 */
	private static de.c_hack.tim.lib.Config conf;

	/**
	 * The main logger instance
	 */
	private static Log log;

	/**
	 * The currently loaded Class to be run.
	 */
	private static AnimationSnippet current;

	/**
	 * *Main*
	 * 
	 * @param args
	 *            The first parameter is the name of the
	 */
	public static void main(String[] args) {
		//Log loading
		try {
			log = new Log(new String[] { "std", "logs/main.log" }, Log.INFO, Log.WARN);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Real start of program
		try {
			run(args);
		} catch (Exception e) {
			log.logException(e, Log.ERROR, true);
		}
	}

	/**
	 * This method runs the whole program. It is called by main directly after
	 * Log is set up.
	 * 
	 * @param args
	 *            The cmd line arguments
	 */
	public static void run(String[] args) {
		//Loading conf
		try {
			log.log("Loading conf...", Log.DEBUG);

			HashMap<String, String> configFields = new HashMap<>();

			configFields.put("Asset_Directory", "assets/");
			configFields.put("DebugLog", "false");
			configFields.put("FlushLog", "false");
			configFields.put("FlushFile", "flush");
			configFields.put("SimulatorFiles", "sim/panel");
			configFields.put("EnableSimulator", "true");
			configFields.put("RunFile", "run");
			configFields.put("StopFile", "stop");

			conf = new Config("c-hack-panels.conf", "Die config der C-Hack Panel", configFields);

			log.log("Done.", Log.DEBUG);

		} catch (IOException e) {
			log.logException(e, Log.ERROR, true);
		}

		if (conf.getConfigValue("DebugLog").contentEquals("true")) log.setLogLevel(Log.DEBUG);
		log.log("Enabled Debug logging...", Log.DEBUG);

		//Setting up run file.
		File runFile = new File(conf.getConfigValue("RunFile"));
		runFile.deleteOnExit();
		String argString = "";
		for (String s : args) {
			argString += s + ";";
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(runFile));
			bw.write(argString);
			bw.flush();
		} catch (NoSuchElementException | IOException e) {
			log.logException(e, Log.ERROR, true);
		} finally {
			if (bw != null) try {
				bw.close();
			} catch (IOException e) {
				log.logException(e, Log.WARN, false);
			}
			bw = null;
		}

		//Real start of program
		if (args.length < 1) {
			log.log("Missing argument. Don't know what animation to run.", Log.ERROR);
			System.exit(1);
		}

		for (Class<? extends AnimationSnippet> c : LoadedSnippets.LOADED_SNIPPETS) {
			if (!c.getPackage().equals(GenericSnippet.class.getPackage())) continue; //So we don't get name conflicts
			if (c.getName().substring(c.getName().lastIndexOf(".") + 1).equals(args[0])) {
				try {
					setCurrent(c.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					log.logException(e, Log.ERROR, true);
				}
			}
		}

		if (getCurrent() == null) {
			log.log("Animation with name " + args[0] + " not found.", Log.ERROR);
			return;
		}

		log.log("Running: \"" + args[0] + "\" with arguments:" + argString, Log.INFO);

		log.log("Running Setup:", Log.DEBUG);
		getCurrent().setup(args);
		log.log("Finished Setup.", Log.DEBUG);

		log.log("Running Loop:", Log.DEBUG);
		//Run until the code file is changed, then stop, because the new one will probably be run.
		while ((!new File(conf.getConfigValue("StopFile")).exists())) {
			getCurrent().loop();
			try {
				Thread.sleep(Math.round(1000 / (getCurrent().getFreq() <= 1000 ? getCurrent().getFreq() : 1000)));
			} catch (InterruptedException e) {
				log.logException(e, Log.WARN, false);
			}
		}
		log.log("Stoped running.", Log.INFO);
	}

	/**
	 * Get's {@link #current current}
	 * 
	 * @return current
	 */
	public static AnimationSnippet getCurrent() {
		return current;
	}

	/**
	 * Set's {@link #current current}
	 * 
	 * @param par_current
	 *            current
	 */
	public static void setCurrent(AnimationSnippet par_current) {
		current = par_current;
	}

	/**
	 * Get's {@link #conf conf}
	 * 
	 * @return conf
	 */
	public static de.c_hack.tim.lib.Config getConf() {
		return conf;
	}

	/**
	 * Set's {@link #conf conf}
	 * 
	 * @param par_conf
	 *            conf
	 */
	public static void setConf(de.c_hack.tim.lib.Config par_conf) {
		conf = par_conf;
	}

	/**
	 * Get's {@link #log log}
	 * 
	 * @return log
	 */
	public static Log getLog() {
		return log;
	}
}
