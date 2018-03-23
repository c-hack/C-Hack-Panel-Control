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
package de.c_hack.tim.cHackPanelControl.animationSnippets;

import de.c_hack.tim.cHackPanelControl.AnimationSnippet;
import de.c_hack.tim.cHackPanelControl.api.PColor;
import de.c_hack.tim.cHackPanelControl.api.Panel;
import de.c_hack.tim.cHackPanelControl.api.Panels;

/**
 * A simple script that fills every panel with a random color every 4
 * seconds.
 * 
 * @author Tim Neumann
 */
public class LedTest1 extends AnimationSnippet {
	/** Internal storage of panels. Each will be filled by it's own color. */
	Panel[] panels = new Panel[11];

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#setup(String[])
	 */
	@Override
	public void setup(String[] args) {
		debug("Setting up");
		this.setFreq(0.25F);
		this.panels[0] = new Panel();
		this.panels[0].fill(PColor.BLACK);
		this.panels[1] = new Panel();
		this.panels[1].fill(PColor.RED);
		this.panels[2] = new Panel();
		this.panels[2].fill(PColor.GREEN);
		this.panels[3] = new Panel();
		this.panels[3].fill(PColor.BLUE);
		this.panels[4] = new Panel();
		this.panels[4].fill(PColor.YELLOW);
		this.panels[5] = new Panel();
		this.panels[5].fill(PColor.VIOLET);
		this.panels[6] = new Panel();
		this.panels[6].fill(PColor.CYAN);
		this.panels[7] = new Panel();
		this.panels[7].fill(PColor.WHITE);
		this.panels[8] = new Panel();
		this.panels[8].fill(PColor.RED);
		this.panels[9] = new Panel();
		this.panels[9].fill(PColor.GREEN);
		this.panels[10] = new Panel();
		this.panels[10].fill(PColor.BLUE);
	}

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#loop()
	 */
	@Override
	public void loop() {
		debug("Tis is loop!");

		double r = Math.random();
		int p1 = (int) Math.round(r * 10);
		r = Math.random();
		int p2 = (int) Math.round(r * 10);
		r = Math.random();
		int p3 = (int) Math.round(r * 10);
		r = Math.random();
		int p4 = (int) Math.round(r * 10);
		r = Math.random();
		int p5 = (int) Math.round(r * 10);
		r = Math.random();
		int p6 = (int) Math.round(r * 10);
		r = Math.random();

		Panels.setPanel(1, this.panels[p1]);
		Panels.setPanel(2, this.panels[p2]);
		Panels.setPanel(3, this.panels[p3]);
		Panels.setPanel(4, this.panels[p4]);
		Panels.setPanel(5, this.panels[p5]);
		Panels.setPanel(6, this.panels[p6]);
		Panels.flush();
	}

}
