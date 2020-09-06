package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Cell extends ColidableCircle {
	private PVector moveTo;
	int rad = 96/2;
	float membraneHealth = 9.9f;
	float energy = 10.0f;
	private Nucleus nucleus;
	Main parent;
	public ArrayList<RNA> rna = new ArrayList<RNA>();
	public ArrayList<VirusShell> shells = new ArrayList<VirusShell>();
	public ArrayList<RNA> rnaToRemove = new ArrayList<RNA>();
	public ArrayList<VirusShell> shellsToRemove = new ArrayList<VirusShell>();
	private ArrayList<Long> infectedBy = new ArrayList<Long>();
	
	public Cell(PVector pos, PVector moveTo, Main parent) {
		this.pos = pos;
		setNucleus(new Nucleus(this));
		this.moveTo = moveTo;
		this.parent = parent;
	}
	
	public void infect(Virus virus) {
		if (infectedBy.contains(virus.hash())) {
			return;
		}
		infectedBy.add(virus.hash());
		this.nucleus.merge(virus.getGenes());
		virus.die();
		parent.score += 100;
	}
	
	public void draw(Main applet, Cam cam) {
		for (RNA r : this.rna) {
			r.draw(applet, cam);
			ArrayList<ColidableCircle> objs = new ArrayList<ColidableCircle>(applet.cells);
			objs.addAll(this.shells);
			r.update(applet, objs, true);
		}
		for (RNA r : this.rnaToRemove) {
			this.rna.remove(r);
		}
		this.rnaToRemove.clear();
		for (VirusShell s : this.shells) {
			s.draw(applet, cam);
			ArrayList<ColidableCircle> objs = new ArrayList<ColidableCircle>(applet.cells);
			objs.addAll(this.rna);
			objs.addAll(this.shells);
			s.update(applet, objs, true);
		}
		for (VirusShell s : this.shellsToRemove) {
			this.shells.remove(s);
		}
		this.shellsToRemove.clear();
	
		
		if (this.membraneHealth < 0) {
			this.parent.kill(this);
			this.parent.score += 50;
			return;
		}
		float scx = getPos().x * cam.zoom + cam.translate.x;
		float scy = getPos().y * cam.zoom + cam.translate.y;
		float scr = getRad() * 2 * cam.zoom;
		if (!(scx > 0 - scr && scx < applet.width + scr
		   && scy > 0 - scr&& scy < applet.height + scr)) {
			return;
		}
		applet.fill(255, 255, 0);
		applet.stroke(0);
		applet.rect(scx - 25 * cam.zoom, scy - 36 * cam.zoom, energy * 5 * cam.zoom, 5 * cam.zoom);
		getNucleus().draw(applet, getPos(), cam);
		if (energy > 0) {
			applet.noFill();
		} else {
			applet.fill(0x40000000);
		}
		applet.strokeWeight(membraneHealth / 3 * cam.zoom);
		applet.circle(scx, scy, scr);
		applet.strokeWeight(1);
	}
	
	public void update(Main applet, ArrayList<ColidableCircle> objs) {
		if (this.pos != this.moveTo) { 
			this.pos.add(PVector.sub(moveTo, pos).div(30));
		}
		for (RNA r : this.rna) {
			r.update(applet, objs, true);
		}
	}
	
	public void tick() {
		if (energy > 0) {
			getNucleus().tick();
		}
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
		this.energy -= 0.3f;
		Cell newCell = new Cell(moveTo.copy(), dir, parent);
		newCell.setNucleus(getNucleus().mutate());
		newCell.getNucleus().parent = newCell;
		newCell.energy = this.energy * 0.8f;
		this.energy *= 0.8f;
		newCell.membraneHealth = this.membraneHealth * 0.8f;
		this.membraneHealth *= 0.8f;
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