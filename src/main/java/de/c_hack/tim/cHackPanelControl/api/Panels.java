/*
 * C-Hack Panel Java Control System
 * 
 * @author Tim Neumann
 * @version 1.0.1
 * @copyright (c) Tim Neumann 2017-2018
 * @license:
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */
package de.c_hack.tim.cHackPanelControl.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.c_hack.tim.cHackPanelControl.Main;
import de.c_hack.tim.lib.Log;

/**
 * The api to communicate with the hardware panels
 * 
 * @author Tim Neumann
 */
public class Panels {
	private static Panel panels[] = new Panel[6];

	private static Log flushLog = null;

	/**
	 * Writes to all panels. <br>
	 * Like calling flush({1,2,3,4,5,6}).
	 */
	public static void flush() {
		flush(new int[] { 1, 2, 3, 4, 5, 6 });
	}

	/**
	 * Writes to a number of panels.
	 * 
	 * @param p_panels
	 *            Which panels to write to (1-6).
	 */
	public static void flush(int[] p_panels) {
		for (int p : p_panels) {
			int addr = p | 128;
			if (panels[p - 1] == null) panels[p - 1] = new Panel();
			int[] data = panels[p - 1].getContentAsBytes();
			int[] bytes = new int[data.length + 1];
			bytes[0] = addr;
			for (int i = 0; i < data.length; i++) {
				bytes[i + 1] = data[i];
			}
			flushB(bytes);

			if (Main.getConf().getConfigValue("EnableSimulator").contentEquals("true")) {
				String fileName = Main.getConf().getConfigValue("SimulatorFiles") + p + ".dat";

				String simData = "";

				for (int y = 0; y < panels[p - 1].content[0].length; y++) {
					for (int x = 0; x < panels[p - 1].content.length; x++) {
						simData += (panels[p - 1].content[x][y][0] ? "1" : "0") + (panels[p - 1].content[x][y][1] ? "1" : "0") + (panels[p - 1].content[x][y][2] ? "1" : "0") + " ";
					}
					simData = simData.substring(0, simData.length() - 1);
					simData += "\n";
				}
				simData = simData.substring(0, simData.length() - 1);

				BufferedWriter bw = null;

				try {
					bw = new BufferedWriter(new FileWriter(fileName, false));
					bw.write(simData);
					bw.flush();
				} catch (IOException e) {
					Main.getLog().logException(e, Log.ERROR, false);
				} finally {
					if (bw != null) try {
						bw.close();
					} catch (IOException e) {
						Main.getLog().logException(e, Log.WARN, false);
					}
					bw = null;
				}
			}
		}
	}

	private static void flushB(int[] bytes) {
		String bytesS = "";
		for (int b : bytes) {
			bytesS += Integer.toString(b) + " ";
		}

		//Flush log
		if (Main.getConf().getConfigValue("FlushLog").contentEquals("true")) {
			if (flushLog == null) try {
				flushLog = new Log(new String[] { "log/flush.log" }, Log.DEBUG);
			} catch (IOException e) {
				Main.getLog().logException(e, Log.ERROR, false);
			}

			if (flushLog != null) {
				flushLog.log(bytesS, Log.INFO);
			}

		}

		BufferedWriter bw = null;

		bytesS += System.getProperty("line.separator");

		try {
			bw = new BufferedWriter(new FileWriter(Main.getConf().getConfigValue("FlushFile"), true));
			bw.write(bytesS);
			bw.flush();
		} catch (IOException e) {
			Main.getLog().logException(e, Log.ERROR, false);
		} finally {
			if (bw != null) try {
				bw.close();
			} catch (IOException e) {
				Main.getLog().logException(e, Log.WARN, false);
			}
			bw = null;
		}
	}

	/**
	 * Get's one of{@link #panels panels}
	 * 
	 * @param index
	 *            The panel to get (1-6).
	 * @return panels
	 */
	public static Panel getPanel(int index) {
		return panels[index - 1];
	}

	/**
	 * Set's one of {@link #panels panels}
	 * 
	 * @param index
	 *            Which panel to set (1-6)
	 * @param par_panel
	 *            The panel representation to set it to
	 */
	public static void setPanel(int index, Panel par_panel) {
		panels[index - 1] = par_panel;
	}

	/**
	 * Get's {@link #panels panels}
	 * 
	 * @return panels
	 */
	public static Panel[] getPanels() {
		return panels;
	}

	/**
	 * Set's {@link #panels panels}
	 * 
	 * @param par_panels
	 *            panels
	 */
	public static void setPanels(Panel[] par_panels) {
		panels = par_panels;
	}

}
