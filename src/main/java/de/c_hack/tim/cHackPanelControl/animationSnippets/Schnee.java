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
 * @author Tim Neumann
 */
public class Schnee extends AnimationSnippet {
	private Panel[] text = new Panel[18];
	private Panel[] display = new Panel[6];
	private Mover movText = new Mover(18 * 4, 7, true, false);
	private Mover movSchnee = new Mover(6 * 4, 7, false, false);

	private int count;
	private int color;

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#setup(java.lang.String[])
	 */
	@Override
	public void setup(String[] args) {
		debug("Setup");
		this.setFreq(6F);

		this.text[0] = new Panel("C", PColor.GREEN);

		this.text[1] = new Panel("-", PColor.CYAN);

		this.color = PColor.BLUE;
		this.text[2] = new Panel("H", this.color);
		this.text[3] = new Panel("a", this.color);
		this.text[4] = new Panel("c", this.color);
		this.text[5] = new Panel("k", this.color);

		this.color = PColor.RED;
		this.text[6] = new Panel("--", this.color);
		this.text[7] = new Panel("M", this.color);
		this.text[8] = new Panel("i", this.color);
		this.text[9] = new Panel("1", this.color);
		this.text[10] = new Panel("9", this.color);
		this.text[11] = new Panel(":", this.color);
		this.text[12] = new Panel("0", this.color);
		this.text[13] = new Panel("0", this.color);
		this.text[14] = new Panel("U", this.color);
		this.text[15] = new Panel("h", this.color);
		this.text[16] = new Panel("r", this.color);
		this.text[17] = new Panel();

		Panels.setPanels(this.text);
		Panels.flush();
		this.movText.setPanelArray(0, 0, 0, this.text);
	}

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#loop()
	 */
	@Override
	public void loop() {
		this.count += 1;

		if (this.count % 3 == 0) {

			this.movText.move(-1, 0);
		}

		this.movSchnee.move(0, 1);
		for (int i = 0; i < this.movSchnee.getWidth(); i++) {
			double r = Math.random();

			if (r < 0.1)
				this.movSchnee.set(i, 0, PColor.WHITE);
		}

		for (int i = 0; i < 6; i++) {
			this.display[i] = new Panel();
			this.display[i].add(this.movSchnee.getPanel(i * 4, 0), Panel.MODE_OVERWRITE);
			this.display[i].add(this.text[i], Panel.MODE_OVERWRITE);
		}

		Panels.setPanels(this.display);
		Panels.flush();
	}
}
