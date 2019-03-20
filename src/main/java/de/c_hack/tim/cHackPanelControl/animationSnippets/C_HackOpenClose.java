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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.c_hack.tim.cHackPanelControl.AnimationSnippet;
import de.c_hack.tim.cHackPanelControl.api.Mover;
import de.c_hack.tim.cHackPanelControl.api.PColor;
import de.c_hack.tim.cHackPanelControl.api.Panel;
import de.c_hack.tim.cHackPanelControl.api.Panels;

/**
 * Animation Snippet for C-Hack open/close
 * 
 * @author Tim Neumann
 */
public class C_HackOpenClose extends AnimationSnippet {
	private static final String stateURL = "https://api.c-hack.de/open-close/now/";

	private boolean lastState = false;
	private Mover mover;
	
	private Panel[] cHackOpen;
	private Panel[] cHackClosed;
	
	private int counter = 0;
	
	private boolean isOpen() {
		URI stateURI;
		try {
			stateURI = new URI(stateURL);
		} catch (URISyntaxException e1) {
			throw new IllegalStateException(e1);
		}
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(stateURI.toURL().openStream()))){
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
				response.append(line);
			return new JSONObject(response.toString()).getBoolean("state");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	private void initMoverOpen() {
		this.mover = new Mover(60, 7, true, true);
		this.mover.setPanelArray(0, 0, 1, cHackOpen);
	}
	
	private void initMoverClosed() {
		this.mover = new Mover(70, 7, true, true);
		this.mover.setPanelArray(0, 0, 1, cHackClosed);
	}
	
	
	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#setup(java.lang.String[])
	 */
	@Override
	public void setup(String[] par_args) {
		debug("Setting up");
		this.setFreq(2F);

		List<Panel> cHack = new ArrayList<>();
		List<Panel> open = new ArrayList<>();
		List<Panel> closed = new ArrayList<>();
		
		cHack.add(new Panel("C", PColor.GREEN));
		cHack.add(new Panel("--", PColor.WHITE));
		cHack.add(new Panel("H", PColor.BLUE));
		cHack.add(new Panel("A", PColor.BLUE));
		cHack.add(new Panel("C", PColor.BLUE));
		cHack.add(new Panel("K", PColor.BLUE));
		
		open.add(new Panel("O", PColor.GREEN));
		open.add(new Panel("p", PColor.GREEN));
		open.add(new Panel("e", PColor.GREEN));
		open.add(new Panel("n", PColor.GREEN));

		closed.add(new Panel("C", PColor.RED));
		closed.add(new Panel("l", PColor.RED));
		closed.add(new Panel("o", PColor.RED));
		closed.add(new Panel("s", PColor.RED));
		closed.add(new Panel("e", PColor.RED));
		closed.add(new Panel("d", PColor.RED));

		List<Panel> cHackOpen = new ArrayList<Panel>(cHack);
		cHackOpen.add(new Panel());
		cHackOpen.addAll(open);
		cHackOpen.add(new Panel());
		this.cHackOpen = cHackOpen.toArray(new Panel[0]);
		
		List<Panel> cHackClosed = new ArrayList<Panel>(cHack);
		cHackClosed.add(new Panel());
		cHackClosed.addAll(closed);
		cHackClosed.add(new Panel());
		this.cHackClosed = cHackClosed.toArray(new Panel[0]);
		
		initMoverClosed();
	}

	/**
	 * @see de.c_hack.tim.cHackPanelControl.AnimationSnippet#loop()
	 */
	@Override
	public void loop() {
		if(counter > 10) {
			counter = 0;
			boolean currentState = isOpen();
			if(lastState != currentState) {
				lastState = currentState;
				if(currentState) {
					initMoverOpen();
				} else {
					initMoverClosed();
				}
			}
		}
		counter ++;
	
		mover.move(-1, 0);
		Panels.setPanels(mover.getPanelArray(0, 0, 1, 6));
		Panels.flush();
	}

}
