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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.c_hack.tim.cHackPanelControl.Main;
import de.c_hack.tim.lib.Log;

/**
 * The representation of a single panel.
 * 
 * @author Tim Neumann
 */
public class Panel {
	/**
	 * Mode to overwrite any existing data(Except if new data is black.)
	 */
	public static final int MODE_OVERWRITE = 1;

	/**
	 * Mode to set each complementary color if both candidates also have it.
	 */
	public static final int MODE_AND = 2;

	/**
	 * Mode to set each complementary color if one of the candidates has it.
	 */
	public static final int MODE_OR = 3;

	/** The content of the panel. */
	boolean[][][] content = new boolean[4][7][3];

	/**
	 * Creates an empty panel
	 */
	public Panel() {

	}

	/**
	 * Creates a panel with the specified resource. Pixels, where the resource
	 * does not
	 * specify a color, will be white.
	 * 
	 * @param name
	 *            The name of the resource in the asset directory.
	 */
	public Panel(String name) {
		this(name, PColor.WHITE);
	}

	/**
	 * Creates a panel with the specified resource. Pixels, where the resource
	 * does not
	 * specify a color, will be given the color color.
	 * 
	 * @param name
	 *            The name of the resource in the asset directory.
	 * @param color
	 *            The color to give to pixels, where no other color is
	 *            specified.
	 */
	public Panel(String name, int color) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(de.c_hack.tim.cHackPanelControl.Main.getConf().getConfigValue("Asset_Directory") + name));
			String line;
			int lineC = 0;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].length() == 1) {
						if (parts[i].contentEquals("1")) {
							this.content[i][lineC][0] = ((color & 0b100) != 0);
							this.content[i][lineC][1] = ((color & 0b010) != 0);
							this.content[i][lineC][2] = ((color & 0b001) != 0);
						}
					}
					else {
						this.content[i][lineC][0] = (parts[i].substring(0, 1).contentEquals("1"));
						this.content[i][lineC][1] = (parts[i].substring(1, 2).contentEquals("1"));
						this.content[i][lineC][2] = (parts[i].substring(2, 3).contentEquals("1"));
					}
				}
				lineC++;
			}
		} catch (

		IOException e) {
			this.content = new boolean[4][7][3];
			if (e instanceof FileNotFoundException) Main.getLog().log("Asset file '" + name + "' not found!", Log.ERROR);
			Main.getLog().logException(e, Log.WARN, false);
		} finally {
			if (reader != null) try {
				reader.close();
			} catch (IOException e) {
				Main.getLog().logException(e, Log.WARN, false);
			}
			reader = null;
		}
	}

	/**
	 * Set's a pixel
	 * 
	 * @param column
	 *            The column of the panel (0-3)
	 * @param row
	 *            The row of the panel (0-6)
	 * @param color
	 *            The color as defined by PColor.
	 * @return it self after the changes. This doeas not need to be used.
	 */
	public Panel setPixel(int column, int row, int color) {
		this.content[column][row][0] = ((color & 0b100) == 1);
		this.content[column][row][1] = ((color & 0b010) == 1);
		this.content[column][row][2] = ((color & 0b001) == 1);
		return this;
	}

	/**
	 * Replaces all pixels of a specific color by another color.
	 * 
	 * @param search
	 *            The color to search for.
	 * @param replace
	 *            The color to replace by.
	 * @return it self after the changes. This doeas not need to be used.
	 */
	public Panel replaceColor(int search, int replace) {
		for (int x = 0; x < this.content.length; x++) {
			for (int y = 0; y < this.content[x].length; y++) {
				if ((this.content[x][y][0] == ((search & 0b100) != 0)) && (this.content[x][y][1] == ((search & 0b010) != 0)) && (this.content[x][y][2] == ((search & 0b001) != 0))) {
					this.content[x][y][0] = ((replace & 0b100) != 0);
					this.content[x][y][1] = ((replace & 0b010) != 0);
					this.content[x][y][2] = ((replace & 0b001) != 0);
				}
			}
		}
		return this;
	}

	/**
	 * Fills the entire Panel with the color.
	 * 
	 * @param color
	 *            The color to fill with.
	 * @return it self after the changes. This doeas not need to be used.
	 */
	public Panel fill(int color) {
		for (int x = 0; x < this.content.length; x++) {
			for (int y = 0; y < this.content[x].length; y++) {
				this.content[x][y][0] = ((color & 0b100) != 0);
				this.content[x][y][1] = ((color & 0b010) != 0);
				this.content[x][y][2] = ((color & 0b001) != 0);
			}
		}
		return this;
	}

	/**
	 * Adds another panel over this one. (As layers and then unites them)
	 * 
	 * @param panel
	 *            The panel to add.
	 * @param mode
	 *            The mode by which to unite. Either MODE_OVERWRITE, MODE_AND or
	 *            MODE_OR. If any other integer is given. It does nothing.
	 * @return it self after the changes. This doeas not need to be used.
	 */
	public Panel add(Panel panel, int mode) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 7; y++) {
				switch (mode) {
					case 1:
						this.content[x][y] = (panel.content[x][y][0] || panel.content[x][y][1] || panel.content[x][y][2] ? panel.content[x][y] : this.content[x][y]);
					break;
					case 2:
						this.content[x][y][0] = this.content[x][y][0] && panel.content[x][y][0];
						this.content[x][y][1] = this.content[x][y][1] && panel.content[x][y][1];
						this.content[x][y][2] = this.content[x][y][2] && panel.content[x][y][2];
					break;
					case 3:
						this.content[x][y][0] = this.content[x][y][0] || panel.content[x][y][0];
						this.content[x][y][1] = this.content[x][y][1] || panel.content[x][y][1];
						this.content[x][y][2] = this.content[x][y][2] || panel.content[x][y][2];
					break;
					default:
					break;
				}
			}
		}
		return this;
	}

	/**
	 * For backend use.
	 * 
	 * @return The panel data as bytes to flush over the Serial.
	 */
	public int[] getContentAsBytes() {
		/*int b1 = (1 & (this.content[0][0][0] ? 1 : 0)) + (2 & (this.content[0][1][0] ? 1 : 0) * 2) + (4 & (this.content[0][2][0] ? 1 : 0) * 4) + (8 & (this.content[0][3][0] ? 1 : 0) * 8) + (16 & (this.content[0][4][0] ? 1 : 0) * 16) + (32 & (this.content[0][5][0] ? 1 : 0) * 32) + (64 & (this.content[0][6][0] ? 1 : 0) * 64);
		int b2 = (1 & (this.content[0][0][1] ? 1 : 0)) + (2 & (this.content[0][1][1] ? 1 : 0) * 2) + (4 & (this.content[0][2][1] ? 1 : 0) * 4) + (8 & (this.content[0][3][1] ? 1 : 0) * 8) + (16 & (this.content[0][4][1] ? 1 : 0) * 16) + (32 & (this.content[0][5][1] ? 1 : 0) * 32) + (64 & (this.content[0][6][1] ? 1 : 0) * 64);
		int b3 = (1 & (this.content[0][0][2] ? 1 : 0)) + (2 & (this.content[0][1][2] ? 1 : 0) * 2) + (4 & (this.content[0][2][2] ? 1 : 0) * 4) + (8 & (this.content[0][3][2] ? 1 : 0) * 8) + (16 & (this.content[0][4][2] ? 1 : 0) * 16) + (32 & (this.content[0][5][2] ? 1 : 0) * 32) + (64 & (this.content[0][6][2] ? 1 : 0) * 64);
		int b4 = (1 & (this.content[1][0][0] ? 1 : 0)) + (2 & (this.content[1][1][0] ? 1 : 0) * 2) + (4 & (this.content[1][2][0] ? 1 : 0) * 4) + (8 & (this.content[1][3][0] ? 1 : 0) * 8) + (16 & (this.content[1][4][0] ? 1 : 0) * 16) + (32 & (this.content[1][5][0] ? 1 : 0) * 32) + (64 & (this.content[1][6][0] ? 1 : 0) * 64);
		int b5 = (1 & (this.content[1][0][1] ? 1 : 0)) + (2 & (this.content[1][1][1] ? 1 : 0) * 2) + (4 & (this.content[1][2][1] ? 1 : 0) * 4) + (8 & (this.content[1][3][1] ? 1 : 0) * 8) + (16 & (this.content[1][4][1] ? 1 : 0) * 16) + (32 & (this.content[1][5][1] ? 1 : 0) * 32) + (64 & (this.content[1][6][1] ? 1 : 0) * 64);
		int b6 = (1 & (this.content[1][0][2] ? 1 : 0)) + (2 & (this.content[1][1][2] ? 1 : 0) * 2) + (4 & (this.content[1][2][2] ? 1 : 0) * 4) + (8 & (this.content[1][3][2] ? 1 : 0) * 8) + (16 & (this.content[1][4][2] ? 1 : 0) * 16) + (32 & (this.content[1][5][2] ? 1 : 0) * 32) + (64 & (this.content[1][6][2] ? 1 : 0) * 64);
		int b7 = (1 & (this.content[2][0][0] ? 1 : 0)) + (2 & (this.content[2][1][0] ? 1 : 0) * 2) + (4 & (this.content[2][2][0] ? 1 : 0) * 4) + (8 & (this.content[2][3][0] ? 1 : 0) * 8) + (16 & (this.content[2][4][0] ? 1 : 0) * 16) + (32 & (this.content[2][5][0] ? 1 : 0) * 32) + (64 & (this.content[2][6][0] ? 1 : 0) * 64);
		int b8 = (1 & (this.content[2][0][1] ? 1 : 0)) + (2 & (this.content[2][1][1] ? 1 : 0) * 2) + (4 & (this.content[2][2][1] ? 1 : 0) * 4) + (8 & (this.content[2][3][1] ? 1 : 0) * 8) + (16 & (this.content[2][4][1] ? 1 : 0) * 16) + (32 & (this.content[2][5][1] ? 1 : 0) * 32) + (64 & (this.content[2][6][1] ? 1 : 0) * 64);
		int b9 = (1 & (this.content[2][0][2] ? 1 : 0)) + (2 & (this.content[2][1][2] ? 1 : 0) * 2) + (4 & (this.content[2][2][2] ? 1 : 0) * 4) + (8 & (this.content[2][3][2] ? 1 : 0) * 8) + (16 & (this.content[2][4][2] ? 1 : 0) * 16) + (32 & (this.content[2][5][2] ? 1 : 0) * 32) + (64 & (this.content[2][6][2] ? 1 : 0) * 64);
		int b10 = (1 & (this.content[3][0][0] ? 1 : 0)) + (2 & (this.content[3][1][0] ? 1 : 0) * 2) + (4 & (this.content[3][2][0] ? 1 : 0) * 4) + (8 & (this.content[3][3][0] ? 1 : 0) * 8) + (16 & (this.content[3][4][0] ? 1 : 0) * 16) + (32 & (this.content[3][5][0] ? 1 : 0) * 32) + (64 & (this.content[3][6][0] ? 1 : 0) * 64);
		int b11 = (1 & (this.content[3][0][1] ? 1 : 0)) + (2 & (this.content[3][1][1] ? 1 : 0) * 2) + (4 & (this.content[3][2][1] ? 1 : 0) * 4) + (8 & (this.content[3][3][1] ? 1 : 0) * 8) + (16 & (this.content[3][4][1] ? 1 : 0) * 16) + (32 & (this.content[3][5][1] ? 1 : 0) * 32) + (64 & (this.content[3][6][1] ? 1 : 0) * 64);
		int b12 = (1 & (this.content[3][0][2] ? 1 : 0)) + (2 & (this.content[3][1][2] ? 1 : 0) * 2) + (4 & (this.content[3][2][2] ? 1 : 0) * 4) + (8 & (this.content[3][3][2] ? 1 : 0) * 8) + (16 & (this.content[3][4][2] ? 1 : 0) * 16) + (32 & (this.content[3][5][2] ? 1 : 0) * 32) + (64 & (this.content[3][6][2] ? 1 : 0) * 64);
		int[] bA = { b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12 };
		return bA;*/

		/*int b1 = (1 & (this.content[0][6][0] ? 1 : 0)) + (2 & (this.content[0][5][0] ? 1 : 0) * 2) + (4 & (this.content[0][4][0] ? 1 : 0) * 4) + (8 & (this.content[0][3][0] ? 1 : 0) * 8) + (16 & (this.content[0][2][0] ? 1 : 0) * 16) + (32 & (this.content[0][1][0] ? 1 : 0) * 32) + (64 & (this.content[0][0][0] ? 1 : 0) * 64);
		int b2 = (1 & (this.content[0][6][1] ? 1 : 0)) + (2 & (this.content[0][5][1] ? 1 : 0) * 2) + (4 & (this.content[0][4][1] ? 1 : 0) * 4) + (8 & (this.content[0][3][1] ? 1 : 0) * 8) + (16 & (this.content[0][2][1] ? 1 : 0) * 16) + (32 & (this.content[0][1][1] ? 1 : 0) * 32) + (64 & (this.content[0][0][1] ? 1 : 0) * 64);
		int b3 = (1 & (this.content[0][6][2] ? 1 : 0)) + (2 & (this.content[0][5][2] ? 1 : 0) * 2) + (4 & (this.content[0][4][2] ? 1 : 0) * 4) + (8 & (this.content[0][3][2] ? 1 : 0) * 8) + (16 & (this.content[0][2][2] ? 1 : 0) * 16) + (32 & (this.content[0][1][2] ? 1 : 0) * 32) + (64 & (this.content[0][0][2] ? 1 : 0) * 64);
		int b4 = (1 & (this.content[1][6][0] ? 1 : 0)) + (2 & (this.content[1][5][0] ? 1 : 0) * 2) + (4 & (this.content[1][4][0] ? 1 : 0) * 4) + (8 & (this.content[1][3][0] ? 1 : 0) * 8) + (16 & (this.content[1][2][0] ? 1 : 0) * 16) + (32 & (this.content[1][1][0] ? 1 : 0) * 32) + (64 & (this.content[1][0][0] ? 1 : 0) * 64);
		int b5 = (1 & (this.content[1][6][1] ? 1 : 0)) + (2 & (this.content[1][5][1] ? 1 : 0) * 2) + (4 & (this.content[1][4][1] ? 1 : 0) * 4) + (8 & (this.content[1][3][1] ? 1 : 0) * 8) + (16 & (this.content[1][2][1] ? 1 : 0) * 16) + (32 & (this.content[1][1][1] ? 1 : 0) * 32) + (64 & (this.content[1][0][1] ? 1 : 0) * 64);
		int b6 = (1 & (this.content[1][6][2] ? 1 : 0)) + (2 & (this.content[1][5][2] ? 1 : 0) * 2) + (4 & (this.content[1][4][2] ? 1 : 0) * 4) + (8 & (this.content[1][3][2] ? 1 : 0) * 8) + (16 & (this.content[1][2][2] ? 1 : 0) * 16) + (32 & (this.content[1][1][2] ? 1 : 0) * 32) + (64 & (this.content[1][0][2] ? 1 : 0) * 64);
		int b7 = (1 & (this.content[2][6][0] ? 1 : 0)) + (2 & (this.content[2][5][0] ? 1 : 0) * 2) + (4 & (this.content[2][4][0] ? 1 : 0) * 4) + (8 & (this.content[2][3][0] ? 1 : 0) * 8) + (16 & (this.content[2][2][0] ? 1 : 0) * 16) + (32 & (this.content[2][1][0] ? 1 : 0) * 32) + (64 & (this.content[2][0][0] ? 1 : 0) * 64);
		int b8 = (1 & (this.content[2][6][1] ? 1 : 0)) + (2 & (this.content[2][5][1] ? 1 : 0) * 2) + (4 & (this.content[2][4][1] ? 1 : 0) * 4) + (8 & (this.content[2][3][1] ? 1 : 0) * 8) + (16 & (this.content[2][2][1] ? 1 : 0) * 16) + (32 & (this.content[2][1][1] ? 1 : 0) * 32) + (64 & (this.content[2][0][1] ? 1 : 0) * 64);
		int b9 = (1 & (this.content[2][6][2] ? 1 : 0)) + (2 & (this.content[2][5][2] ? 1 : 0) * 2) + (4 & (this.content[2][4][2] ? 1 : 0) * 4) + (8 & (this.content[2][3][2] ? 1 : 0) * 8) + (16 & (this.content[2][2][2] ? 1 : 0) * 16) + (32 & (this.content[2][1][2] ? 1 : 0) * 32) + (64 & (this.content[2][0][2] ? 1 : 0) * 64);
		int b10 = (1 & (this.content[3][6][0] ? 1 : 0)) + (2 & (this.content[3][5][0] ? 1 : 0) * 2) + (4 & (this.content[3][4][0] ? 1 : 0) * 4) + (8 & (this.content[3][3][0] ? 1 : 0) * 8) + (16 & (this.content[3][2][0] ? 1 : 0) * 16) + (32 & (this.content[3][1][0] ? 1 : 0) * 32) + (64 & (this.content[3][0][0] ? 1 : 0) * 64);
		int b11 = (1 & (this.content[3][6][1] ? 1 : 0)) + (2 & (this.content[3][5][1] ? 1 : 0) * 2) + (4 & (this.content[3][4][1] ? 1 : 0) * 4) + (8 & (this.content[3][3][1] ? 1 : 0) * 8) + (16 & (this.content[3][2][1] ? 1 : 0) * 16) + (32 & (this.content[3][1][1] ? 1 : 0) * 32) + (64 & (this.content[3][0][1] ? 1 : 0) * 64);
		int b12 = (1 & (this.content[3][6][2] ? 1 : 0)) + (2 & (this.content[3][5][2] ? 1 : 0) * 2) + (4 & (this.content[3][4][2] ? 1 : 0) * 4) + (8 & (this.content[3][3][2] ? 1 : 0) * 8) + (16 & (this.content[3][2][2] ? 1 : 0) * 16) + (32 & (this.content[3][1][2] ? 1 : 0) * 32) + (64 & (this.content[3][0][2] ? 1 : 0) * 64);
		int[] bA = { b10, b11, b12, b7, b8, b9, b4, b5, b6, b1, b2, b3 };
		return bA;*/

		int b1 = (1 & (this.content[0][6][0] ? 1 : 0)) + (2 & (this.content[0][5][0] ? 1 : 0) * 2) + (4 & (this.content[0][4][0] ? 1 : 0) * 4) + (8 & (this.content[0][3][0] ? 1 : 0) * 8) + (16 & (this.content[0][2][0] ? 1 : 0) * 16) + (32 & (this.content[0][1][0] ? 1 : 0) * 32) + (64 & (this.content[0][0][0] ? 1 : 0) * 64);
		int b2 = (1 & (this.content[0][6][1] ? 1 : 0)) + (2 & (this.content[0][5][1] ? 1 : 0) * 2) + (4 & (this.content[0][4][1] ? 1 : 0) * 4) + (8 & (this.content[0][3][1] ? 1 : 0) * 8) + (16 & (this.content[0][2][1] ? 1 : 0) * 16) + (32 & (this.content[0][1][1] ? 1 : 0) * 32) + (64 & (this.content[0][0][1] ? 1 : 0) * 64);
		int b3 = (1 & (this.content[0][6][2] ? 1 : 0)) + (2 & (this.content[0][5][2] ? 1 : 0) * 2) + (4 & (this.content[0][4][2] ? 1 : 0) * 4) + (8 & (this.content[0][3][2] ? 1 : 0) * 8) + (16 & (this.content[0][2][2] ? 1 : 0) * 16) + (32 & (this.content[0][1][2] ? 1 : 0) * 32) + (64 & (this.content[0][0][2] ? 1 : 0) * 64);
		int b4 = (1 & (this.content[1][6][0] ? 1 : 0)) + (2 & (this.content[1][5][0] ? 1 : 0) * 2) + (4 & (this.content[1][4][0] ? 1 : 0) * 4) + (8 & (this.content[1][3][0] ? 1 : 0) * 8) + (16 & (this.content[1][2][0] ? 1 : 0) * 16) + (32 & (this.content[1][1][0] ? 1 : 0) * 32) + (64 & (this.content[1][0][0] ? 1 : 0) * 64);
		int b5 = (1 & (this.content[1][6][1] ? 1 : 0)) + (2 & (this.content[1][5][1] ? 1 : 0) * 2) + (4 & (this.content[1][4][1] ? 1 : 0) * 4) + (8 & (this.content[1][3][1] ? 1 : 0) * 8) + (16 & (this.content[1][2][1] ? 1 : 0) * 16) + (32 & (this.content[1][1][1] ? 1 : 0) * 32) + (64 & (this.content[1][0][1] ? 1 : 0) * 64);
		int b6 = (1 & (this.content[1][6][2] ? 1 : 0)) + (2 & (this.content[1][5][2] ? 1 : 0) * 2) + (4 & (this.content[1][4][2] ? 1 : 0) * 4) + (8 & (this.content[1][3][2] ? 1 : 0) * 8) + (16 & (this.content[1][2][2] ? 1 : 0) * 16) + (32 & (this.content[1][1][2] ? 1 : 0) * 32) + (64 & (this.content[1][0][2] ? 1 : 0) * 64);
		int b7 = (1 & (this.content[2][6][0] ? 1 : 0)) + (2 & (this.content[2][5][0] ? 1 : 0) * 2) + (4 & (this.content[2][4][0] ? 1 : 0) * 4) + (8 & (this.content[2][3][0] ? 1 : 0) * 8) + (16 & (this.content[2][2][0] ? 1 : 0) * 16) + (32 & (this.content[2][1][0] ? 1 : 0) * 32) + (64 & (this.content[2][0][0] ? 1 : 0) * 64);
		int b8 = (1 & (this.content[2][6][1] ? 1 : 0)) + (2 & (this.content[2][5][1] ? 1 : 0) * 2) + (4 & (this.content[2][4][1] ? 1 : 0) * 4) + (8 & (this.content[2][3][1] ? 1 : 0) * 8) + (16 & (this.content[2][2][1] ? 1 : 0) * 16) + (32 & (this.content[2][1][1] ? 1 : 0) * 32) + (64 & (this.content[2][0][1] ? 1 : 0) * 64);
		int b9 = (1 & (this.content[2][6][2] ? 1 : 0)) + (2 & (this.content[2][5][2] ? 1 : 0) * 2) + (4 & (this.content[2][4][2] ? 1 : 0) * 4) + (8 & (this.content[2][3][2] ? 1 : 0) * 8) + (16 & (this.content[2][2][2] ? 1 : 0) * 16) + (32 & (this.content[2][1][2] ? 1 : 0) * 32) + (64 & (this.content[2][0][2] ? 1 : 0) * 64);
		int b10 = (1 & (this.content[3][6][0] ? 1 : 0)) + (2 & (this.content[3][5][0] ? 1 : 0) * 2) + (4 & (this.content[3][4][0] ? 1 : 0) * 4) + (8 & (this.content[3][3][0] ? 1 : 0) * 8) + (16 & (this.content[3][2][0] ? 1 : 0) * 16) + (32 & (this.content[3][1][0] ? 1 : 0) * 32) + (64 & (this.content[3][0][0] ? 1 : 0) * 64);
		int b11 = (1 & (this.content[3][6][1] ? 1 : 0)) + (2 & (this.content[3][5][1] ? 1 : 0) * 2) + (4 & (this.content[3][4][1] ? 1 : 0) * 4) + (8 & (this.content[3][3][1] ? 1 : 0) * 8) + (16 & (this.content[3][2][1] ? 1 : 0) * 16) + (32 & (this.content[3][1][1] ? 1 : 0) * 32) + (64 & (this.content[3][0][1] ? 1 : 0) * 64);
		int b12 = (1 & (this.content[3][6][2] ? 1 : 0)) + (2 & (this.content[3][5][2] ? 1 : 0) * 2) + (4 & (this.content[3][4][2] ? 1 : 0) * 4) + (8 & (this.content[3][3][2] ? 1 : 0) * 8) + (16 & (this.content[3][2][2] ? 1 : 0) * 16) + (32 & (this.content[3][1][2] ? 1 : 0) * 32) + (64 & (this.content[3][0][2] ? 1 : 0) * 64);
		int[] bA = { b10, b12, b11, b7, b9, b8, b4, b6, b5, b1, b3, b2 };
		return bA;
	}
}
