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
import de.c_hack.tim.cHackPanelControl.api.Panel;
import de.c_hack.tim.cHackPanelControl.api.Panels;

/**
 * This just turns off all leds
 * 
 * @author Tim Neumann
 */
public class AllBlack extends AnimationSnippet {

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#setup(java.lang.String[])
	 */
	@Override
	public void setup(String[] par_args) {
		Panel black = new Panel();
		Panels.setPanel(1, black);
		Panels.setPanel(2, black);
		Panels.setPanel(3, black);
		Panels.setPanel(4, black);
		Panels.setPanel(5, black);
		Panels.setPanel(6, black);
		Panels.flush();
	}

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#loop()
	 */
	@Override
	public void loop() {
		//empty
	}

}
