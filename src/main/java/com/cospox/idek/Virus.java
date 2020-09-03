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
		
		float angle = 0;
		for (Gene g : getGenes()) {
			g.draw(applet, pos,
					20 * PApplet.TWO_PI / getGenes().size(),
					angle, getGenes().size(), cam);
			angle += PApplet.TWO_PI / getGenes().size();
		}
		
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

	public void setGenes(ArrayList<Gene> genes2) {
		this.genes = genes2;
	}
}
