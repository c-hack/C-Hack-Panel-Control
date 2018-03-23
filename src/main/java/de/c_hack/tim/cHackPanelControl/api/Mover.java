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
 * A mover which can simulate moving images/text across the panels.
 * 
 * @author Tim Neumann
 */
public class Mover {
	private int height;
	private int width;
	private boolean[][][] content;
	private boolean reEnterHorizontal;
	private boolean reEnterVertical;

	/**
	 * Creates a new Mover.
	 * 
	 * @param p_width
	 *            The width of the mover space
	 * @param p_height
	 *            The height of the mover space
	 * @param p_reEnterHorizontal
	 *            Whether to reEnter on the other side when exiting on the left
	 *            or right
	 * @param p_reEnterVertical
	 *            Whether to reEnter on the other side when exiting on the top
	 *            or bottom
	 */
	public Mover(int p_width, int p_height, boolean p_reEnterHorizontal, boolean p_reEnterVertical) {
		this.height = p_height;
		this.width = p_width;
		this.reEnterHorizontal = p_reEnterHorizontal;
		this.reEnterVertical = p_reEnterVertical;
		this.content = new boolean[this.width][this.height][3];
	}

	/**
	 * Set's one pixel of the mover space
	 * 
	 * @param column
	 *            The column
	 * @param row
	 *            The row
	 * @param color
	 *            The color
	 */
	public void set(int column, int row, int color) {
		this.content[column][row][0] = ((color & 0b100) == 1);
		this.content[column][row][1] = ((color & 0b010) == 1);
		this.content[column][row][2] = ((color & 0b001) == 1);
	}

	/**
	 * Set's a rectangle of the mover space by a panel
	 * 
	 * @param xOff
	 *            Where the changes should start from the left
	 * @param yOff
	 *            Where the changes should start from the top
	 * @param panel
	 *            The panel with the data to use.
	 */
	public void setPanel(int xOff, int yOff, Panel panel) {
		for (int x = 0; x < panel.content.length; x++) {
			for (int y = 0; y < panel.content[x].length; y++) {
				this.content[x + xOff][y + yOff] = panel.content[x][y];
			}
		}
	}

	/**
	 * Set's a rectangle of the mover space by a array of panels
	 * 
	 * @param xOff
	 *            Where the changes should start from the left
	 * @param yOff
	 *            Where the changes should start from the top
	 * @param space
	 *            The empty vertical lines between the panels
	 * @param panels
	 *            The panel array with the data to use.
	 */

	public void setPanelArray(int xOff, int yOff, int space, Panel[] panels) {
		for (int i = 0; i < panels.length; i++) {
			setPanel(xOff + (i * (4 + space)), yOff, panels[i]);
		}
	}

	/**
	 * Get's a panel from the data of the mover space at the given location
	 * 
	 * @param xOff
	 *            The x position of the data.
	 * @param yOff
	 *            The y position of the data.
	 * @return A new Panel with the data from the mover space at this location.
	 */
	public Panel getPanel(int xOff, int yOff) {
		Panel panel = new Panel();
		for (int x = 0; x < panel.content.length; x++) {
			for (int y = 0; y < panel.content[x].length; y++) {
				panel.content[x][y] = this.content[x + xOff][y + yOff];
			}
		}
		return (panel);
	}

	/**
	 * Get's a panel array from the data of the mover space at the given
	 * location
	 * 
	 * @param xOff
	 *            The x position of the data.
	 * @param yOff
	 *            The y position of the data.
	 * @param space
	 *            The space to ignore between the panels.
	 * @param amount
	 *            How many panels to get.
	 * @return A new Panel with the data from the mover space at this location.
	 */
	public Panel[] getPanelArray(int xOff, int yOff, int space, int amount) {
		Panel[] panels = new Panel[amount];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = getPanel(xOff + (i * (4 + space)), yOff);
		}
		return (panels);
	}

	/**
	 * Moves the complete data of the mover space by a given distance.
	 * 
	 * @param xMove
	 *            the distance to move to the right
	 * @param yMove
	 *            the distance to move to the bottom
	 */
	public void move(int xMove, int yMove) {
		boolean[][][] newContent = new boolean[this.width][this.height][3];
		for (int x = 0; x < this.content.length; x++) {
			for (int y = 0; y < this.content[x].length; y++) {
				int newX = x + xMove;
				int newY = y + yMove;

				if (newX >= this.width) {
					if (this.reEnterHorizontal)
						newX = newX - this.width;
					else
						continue;
				}

				if (newX < 0) {
					if (this.reEnterHorizontal)
						newX = newX + this.width;
					else
						continue;
				}

				if (newY >= this.height) {
					if (this.reEnterVertical)
						newY = newY - this.height;
					else
						continue;
				}

				if (newY < 0) {
					if (this.reEnterVertical)
						newY = newY + this.height;
					else
						continue;
				}

				newContent[newX][newY] = this.content[x][y];
			}
		}
		this.content = newContent;
	}

	/**
	 * Get's {@link #height height}
	 * 
	 * @return height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Get's {@link #width width}
	 * 
	 * @return width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get's {@link #reEnterHorizontal reEnterHorizontal}
	 * 
	 * @return reEnterHorizontal
	 */
	public boolean isReEnterHorizontal() {
		return this.reEnterHorizontal;
	}

	/**
	 * Get's {@link #reEnterVertical reEnterVertical}
	 * 
	 * @return reEnterVertical
	 */
	public boolean isReEnterVertical() {
		return this.reEnterVertical;
	}
}
