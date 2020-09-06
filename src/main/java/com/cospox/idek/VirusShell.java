package com.cospox.idek;

import processing.core.PVector;

public class VirusShell extends ColidableCircle {
	Cell parent;
	public VirusShell(Cell parent, PVector pos) {
		this.parent = parent;
		this.pos = pos;
		this.vel = PVector.random2D();
	}
	public void die() {
		this.parent.shellsToRemove.add(this);
	}
}
