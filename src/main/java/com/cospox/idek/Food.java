package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Food extends ColidableCircle {
	public Food(PVector pos, PVector vel) {
		this.pos = pos;
		this.vel = vel;
		rad = 3;
	}
	
	public void draw(PApplet applet, Cam cam) {
		applet.fill(0x40, 0xA0, 0x10);
		applet.noStroke();
		super.draw(applet, cam);
		applet.noFill();
	}
}
