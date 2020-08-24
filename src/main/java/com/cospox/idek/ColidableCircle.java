package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class ColidableCircle {
	protected PVector pos;
	protected PVector vel;
	protected float rad = 15;
	
	public void draw(PApplet applet, ArrayList<ColidableCircle> objs) {
		applet.circle(pos.x, pos.y, rad * 2);
		this.pos.add(this.vel);
		for (ColidableCircle c : objs) {
			
			float distance = PApplet.dist(pos.x, pos.y, c.getPos().x, c.getPos().y);
			if (distance > c.getRad() + rad && distance < (c.getRad() + rad + 1)) {
			      PVector normal = c.getPos().copy().sub(this.pos);
			      normal.normalize();
			      float f = 2 * PVector.dot(this.vel, normal);
			      PVector reflected = PVector.mult(normal, f);
			      this.vel.sub(reflected);
			}
		}
	}
	
	public float getRad() {
		return rad;
	}
	
	public PVector getPos() {
		return pos;
	}
}
