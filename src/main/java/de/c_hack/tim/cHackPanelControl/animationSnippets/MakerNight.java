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
package de.c_hack.tim.cHackPanelControl.animationSnippets;

import de.c_hack.tim.cHackPanelControl.AnimationSnippet;
import de.c_hack.tim.cHackPanelControl.api.Panel;
import de.c_hack.tim.cHackPanelControl.api.Panels;

/**
 * Siwtches between the words "Maker-" and "night!"
 * 
 * @author Tim Neumann
 */
public class MakerNight extends AnimationSnippet {
	private Panel[] maker;
	private Panel[] night;

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#setup(java.lang.String[])
	 */
	@Override
	public void setup(String[] args) {
		debug("Setting up");
		this.setFreq(0.25F);
		this.maker = new Panel[6];
		this.night = new Panel[6];
		this.maker[0] = new Panel("M");
		this.maker[1] = new Panel("a");
		this.maker[2] = new Panel("k");
		this.maker[3] = new Panel("e");
		this.maker[4] = new Panel("r");
		this.maker[5] = new Panel("--");
		this.night[0] = new Panel("n");
		this.night[1] = new Panel("i");
		this.night[2] = new Panel("g");
		this.night[3] = new Panel("h");
		this.night[4] = new Panel("t");
		this.night[5] = new Panel("!");
	}

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#loop()
	 */
	@Override
	public void loop() {
		debug("Tis is loop!");
		Panels.setPanels(this.maker);
		Panels.flush();
		this.sleep(2);
		Panels.setPanels(this.night);
		Panels.flush();
	}

}
