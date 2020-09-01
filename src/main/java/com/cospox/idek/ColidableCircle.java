package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class ColidableCircle {
	protected PVector pos;
	protected PVector vel;
	protected float rad = 15;
	private float epsilon = 4;
	
	public void draw(PApplet applet, Cam cam) {
		applet.circle(pos.x * cam.zoom + cam.translate.x, pos.y * cam.zoom + cam.translate.y, rad * 2 * cam.zoom);
	}
	
	public void update(PApplet applet, ArrayList<ColidableCircle> objs, boolean collide) {
		this.pos.add(PVector.div(this.vel, applet.frameRate / 60));
		epsilon = 1 / (applet.frameRate / 60);
		for (ColidableCircle c : objs) {
			
			double distance = Math.pow(c.getPos().x - pos.x, 2) + Math.pow(c.getPos().y - pos.y, 2);
			if (distance > Math.pow(c.getRad() + rad - epsilon/2, 2) &&
					distance < Math.pow(c.getRad() + rad + epsilon/2, 2)) {
				if (c instanceof Cell) {
					((Cell)c).membraneHealth -= 0.1;
					if (this instanceof Virus) {
						((Cell)c).getNucleus().merge(((Virus)this).getGenes());
						((Virus)this).die();
					}
				}
				if (!collide) return;
			      PVector normal = c.getPos().copy().sub(this.pos);
			      normal.normalize();
			      float f = 2 * PVector.dot(this.vel, normal);
			      PVector reflected = PVector.mult(normal, f);
			      this.vel.sub(reflected);
			}
		}
		if (this.pos.x > Main.boundry.x) this.pos.x = 0;
		if (this.pos.x < 0) this.pos.x = Main.boundry.x;
		if (this.pos.y > Main.boundry.y) this.pos.y = 0;
		if (this.pos.y < 0) this.pos.y = Main.boundry.y;
	}
	
	public float getRad() {
		return rad;
	}
	
	public PVector getPos() {
		return pos;
	}
}
