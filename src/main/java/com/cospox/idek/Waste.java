package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Waste extends ColidableCircle {
	public Waste(PVector pos, PVector vel) {
		this.pos = pos;
		this.vel = vel;
		rad = 3;
	}
	
	public void draw(PApplet applet, Cam cam) {
		applet.fill(0x80, 0x40, 0x00);
		super.draw(applet, cam);
		applet.noFill();
	}
}
