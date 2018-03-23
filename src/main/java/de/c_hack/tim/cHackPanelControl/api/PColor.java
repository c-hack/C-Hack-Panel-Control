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

/**
 * The definition all usable colors for the panels
 * 
 * @author Tim Neumann
 */
@SuppressWarnings("javadoc")
public class PColor {
	public static final int BLACK = 0b000;
	public static final int RED = 0b100;
	public static final int GREEN = 0b010;
	public static final int BLUE = 0b001;
	public static final int YELLOW = 0b110;
	public static final int VIOLET = 0b101;
	public static final int CYAN = 0b011;
	public static final int WHITE = 0b111;

	/**
	 * Get's the color from a string like "010" for green or "111" for white
	 * 
	 * @param binaryColor
	 *            The String with the color.
	 * @return The color
	 */
	public static int getColorFromBinaryString(String binaryColor) {
		String part1 = binaryColor.substring(0, 1);
		String part2 = binaryColor.substring(1, 2);
		String part3 = binaryColor.substring(2, 3);

		int color = 0;

		if (part1 == "1") color += 0b100;
		if (part2 == "1") color += 0b010;
		if (part3 == "1") color += 0b001;

		return color;
	}
}
