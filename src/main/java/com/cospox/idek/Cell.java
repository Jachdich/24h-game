package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Cell extends ColidableCircle {
	private PVector moveTo;
	private int rad = 96/2;
	float membraneHealth = 9.9f;
	private Nucleus nucleus;
	private Main parent;
	public RNA rna = null;
	
	public Cell(PVector pos, PVector moveTo, Main parent) {
		this.pos = pos;
		setNucleus(new Nucleus(this));
		this.moveTo = moveTo;
		this.parent = parent;
	}
	
	public void draw(PApplet applet, Cam cam) {
		if (this.rna != null) {
			this.rna.draw(applet, cam);
		}
		if (this.membraneHealth < 0) {
			this.parent.kill(this);
			return;
		}
		float scx = getPos().x * cam.zoom + cam.translate.x;
		float scy = getPos().y * cam.zoom + cam.translate.y;
		float scr = getRad() * 2 * cam.zoom;
		if (!(scx > 0 - scr && scx < applet.width + scr
		   && scy > 0 - scr&& scy < applet.height + scr)) {
			return;
		}
		applet.stroke(0);
		applet.noFill();
		applet.strokeWeight(membraneHealth / 3 * cam.zoom);
		applet.circle(scx, scy, scr);
		applet.strokeWeight(1);
		getNucleus().draw(applet, getPos(), cam);
	}
	
	public void update(PApplet applet, ArrayList<ColidableCircle> objs) {
		if (this.pos != this.moveTo) { 
			this.pos.add(PVector.sub(moveTo, pos).div(30));
		}
		if (this.rna != null) {
			this.rna.update(applet, objs, true);
		}
	}
	
	public void tick() {
		getNucleus().tick();
	}
	
	public void divide(boolean respectMax) {
		if (respectMax && this.parent.cells.size() >= this.parent.targetCells) {
			return;
		}
		float angle = parent.random(0, PConstants.TWO_PI);
		PVector dir = PVector.fromAngle(angle);
		dir.mult(rad * 2);
		dir.add(moveTo);
		if (dir.x < rad || dir.y < rad || dir.x > (Main.boundry.x - rad) || dir.y > (Main.boundry.y - rad)) return;
		for (Cell c : parent.cells) {
			if (PApplet.dist(c.moveTo.x, c.moveTo.y, dir.x, dir.y) < rad * 2) {
				return;
			}
		}
		Cell newCell = new Cell(moveTo.copy(), dir, parent);
		newCell.setNucleus(getNucleus().mutate());
		newCell.getNucleus().parent = newCell;
		parent.addCell(newCell);
	}
	
	public boolean isInside(int x, int y, Cam cam) {
		double dx = x - (this.pos.x * cam.zoom + cam.translate.x),
			   dy = y - (this.pos.y * cam.zoom + cam.translate.y);
		return Math.sqrt(dx * dx + dy * dy) <= this.rad * cam.zoom;
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

	public Nucleus getNucleus() {
		return nucleus;
	}

	public void setNucleus(Nucleus nucleus) {
		this.nucleus = nucleus;
	}
}