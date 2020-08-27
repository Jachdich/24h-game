package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;

public class RNA extends ColidableCircle {
	ArrayList<Gene> genes;
	public RNA(ArrayList<Gene> genes) {
		this.genes = genes;
		this.rad = 2;
	}
	public void draw(PApplet applet, ArrayList<ColidableCircle> objs, Cam cam) {
		super.draw(applet, objs, cam, true);
	}
}
