package com.cospox.idek;

import processing.core.PApplet;
import processing.core.PVector;

public class Cell {
	private PVector pos;
	private int rad = 96;
	float membraneHealth = 3;
	private Nucleus nucleus;
	private Main parent;
	
	public Cell(PVector pos, Main parent) {
		this.pos = pos;
		nucleus = new Nucleus(this);
		this.parent = parent;
	}
	
	public void draw(PApplet applet) {
		applet.strokeWeight(membraneHealth);
		applet.circle(pos.x, pos.y, rad);
		applet.strokeWeight(1);
		nucleus.draw(applet, pos);
	}
	public void tick() {
		nucleus.tick();
	}
	
	public void divide() {
		Cell newCell = new Cell(PVector.add(pos, new PVector(rad * 2, 0)), parent);
		newCell.nucleus = nucleus.mutate();
		newCell.nucleus.parent = newCell;
		parent.addCell(newCell);
	}
}
