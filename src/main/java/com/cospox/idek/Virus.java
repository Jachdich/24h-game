package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Virus extends ColidableCircle {
	private ArrayList<Gene> genes = new ArrayList<Gene>();
	public Virus(PVector pos) {
		this.pos = pos;
		this.vel = PVector.random2D();
		this.rad = 15;
	}
	
	public void draw(PApplet applet, ArrayList<ColidableCircle> objs) {
		super.draw(applet, objs);
	}
}
