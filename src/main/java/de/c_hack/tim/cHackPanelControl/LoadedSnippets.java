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

import java.util.ArrayList;
import java.util.List;

import de.c_hack.tim.cHackPanelControl.animationSnippets.AllBlack;
import de.c_hack.tim.cHackPanelControl.animationSnippets.FancyMovingOpen;
import de.c_hack.tim.cHackPanelControl.animationSnippets.LedTest1;
import de.c_hack.tim.cHackPanelControl.animationSnippets.MakerNight;
import de.c_hack.tim.cHackPanelControl.animationSnippets.Schnee;

/**
 * This class defines which snippets to load.
 * 
 * @author Tim Neumann
 */
public class LoadedSnippets {
	/**
	 * All loaded animation snippets
	 */
	public static final List<Class<? extends AnimationSnippet>> LOADED_SNIPPETS = new ArrayList<>();

	static {
		LOADED_SNIPPETS.add(LedTest1.class);
		LOADED_SNIPPETS.add(FancyMovingOpen.class);
		LOADED_SNIPPETS.add(MakerNight.class);
		LOADED_SNIPPETS.add(Schnee.class);
		LOADED_SNIPPETS.add(AllBlack.class);
	}
}
