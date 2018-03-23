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
import de.c_hack.tim.cHackPanelControl.api.Mover;
import de.c_hack.tim.cHackPanelControl.api.PColor;
import de.c_hack.tim.cHackPanelControl.api.Panel;
import de.c_hack.tim.cHackPanelControl.api.Panels;

/**
 * This script moves the words "C-Hack open" right to left and top to bottom
 * 
 * @author Tim Neumann
 */
public class C_Hack extends AnimationSnippet {
	private Mover mov;

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#setup(String[])
	 */
	@Override
	public void setup(String[] args) {
		debug("Setting up");
		this.setFreq(2F);
		Panel[] panels = new Panel[8];
		panels[0] = new Panel("C");
		panels[0].replaceColor(PColor.WHITE, PColor.GREEN);
		panels[1] = new Panel("--");
		panels[1].replaceColor(PColor.WHITE, PColor.WHITE);
		panels[2] = new Panel("H");
		panels[2].replaceColor(PColor.WHITE, PColor.BLUE);
		panels[3] = new Panel("A");
		panels[3].replaceColor(PColor.WHITE, PColor.BLUE);
		panels[4] = new Panel("C");
		panels[4].replaceColor(PColor.WHITE, PColor.BLUE);
		panels[5] = new Panel("K");
		panels[5].replaceColor(PColor.WHITE, PColor.BLUE);
		panels[6] = new Panel();
		panels[7] = new Panel();

		this.mov = new Mover(40, 7, true, true);
		this.mov.setPanelArray(0, 0, 1, panels);
	}

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#loop()
	 */
	@Override
	public void loop() {

		this.mov.move(-1, 0);

		Panels.setPanels(this.mov.getPanelArray(0, 0, 1, 6));
		Panels.flush();
	}

}
