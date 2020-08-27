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
		genes.add(new Gene(GeneType.REPAIR_MEMBRANE));
	}
	
	public void draw(Main applet, ArrayList<ColidableCircle> objs, Cam cam) {
		super.draw(applet, objs, cam, true);
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
