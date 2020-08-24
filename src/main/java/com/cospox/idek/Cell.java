package com.cospox.idek;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Cell extends ColidableCircle {
	private PVector pos;
	private int rad = 96/2;
	float membraneHealth = 3;
	private Nucleus nucleus;
	private Main parent;
	
	public Cell(PVector pos, Main parent) {
		this.setPos(pos);
		nucleus = new Nucleus(this);
		this.parent = parent;
	}
	
	public void draw(PApplet applet) {
		applet.strokeWeight(membraneHealth);
		2applet.circle(getPos().x, getPos().y, getRad() * 2);
		applet.strokeWeight(1);
		nucleus.draw(applet, getPos());
	}
	public void tick() {
		nucleus.tick();
	}
	
	public void divide() {
		float angle = parent.random(0, PConstants.TWO_PI);
		PVector dir = PVector.fromAngle(angle);
		dir.mult(rad * 2);
		dir.add(pos);
		for (Cell c : parent.cells) {
			if (PApplet.dist(c.getPos().x, c.getPos().y, dir.x, dir.y) < rad * 2) {
				return;
			}
		}
		Cell newCell = new Cell(dir, parent);
		newCell.nucleus = nucleus.mutate();
		newCell.nucleus.parent = newCell;
		parent.addCell(newCell);
	}

	public PVector getPos() {
		return pos;
	}

	public void setPos(PVector pos) {
		this.pos = pos;
	}

	public float getRad() {
		return rad;
	}

	public void setRad(int rad) {
		this.rad = rad;
	}
}
