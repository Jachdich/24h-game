package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Virus extends ColidableCircle {
	private boolean dead = false;
	private ArrayList<Gene> genes = new ArrayList<Gene>();
	public Virus(PVector pos) {
		this.pos = pos;
		this.vel = PVector.random2D();
		this.rad = 15;
	}
	
	public void draw(Main applet, Cam cam) {
		applet.noFill();
		super.draw(applet, cam);
		if (dead) {
			applet.kill(this);
		}
	}

	public ArrayList<Gene> getGenes() {
		return genes;
	}

	public void die() {
		this.dead = true;
	}
}
