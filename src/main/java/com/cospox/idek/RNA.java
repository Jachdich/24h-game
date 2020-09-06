package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;

public class RNA extends ColidableCircle {
	ArrayList<Gene> genes;
	Cell parent;
	public RNA(ArrayList<Gene> genes, Cell parent) {
		this.genes = genes;
		this.rad = 2;
		this.parent = parent;
	}
	public void draw(PApplet applet, Cam cam) {
		super.draw(applet, cam);
	}
	public void die() {
		this.parent.rnaToRemove.add(this);
	}
}
