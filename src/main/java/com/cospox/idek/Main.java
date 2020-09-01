//4:58:00

package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Main extends PApplet {
	ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToAdd = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToRemove = new ArrayList<Cell>();
	private ArrayList<Virus> viruses = new ArrayList<Virus>();
	private ArrayList<Virus> virusesToRemove = new ArrayList<Virus>();
	private ArrayList<Food> food = new ArrayList<Food>();
	private ArrayList<Waste> waste = new ArrayList<Waste>();
	private Cam cam;
	private HUD hud;
	private int framesSinceLastTick = 0;
	public int targetCells = 30;
			
	public static void main(String[] args) {
		PApplet.main("com.cospox.idek.Main");
	}
	
	public void settings() {
		size(500, 500);
		smooth(10);
	}
	
	public void setup() {
		cam = new Cam();
		cells.add(new Cell(new PVector(10, 10), new PVector(10, 10), this));
		hud = new HUD(this);
	}
	
	public void addCell(Cell c) {
		cellsToAdd.add(c);
	}
	
	public void draw() {
		background(255);
		fill(0);
		stroke(0);
		text(frameRate, 10, 10);
		//pushMatrix();
		//translate(this.translate.x, this.translate.y);
		//scale(zoom);
		for (Cell c: cells) {
			c.draw(this, new ArrayList<ColidableCircle>(cells), cam);
		}

		for (Virus v: viruses) {
			v.draw(this, new ArrayList<ColidableCircle>(cells), cam);
		}
		for (Food p : food) {
			p.draw(this, new ArrayList<ColidableCircle>(cells), cam, true);
		}
		for (Waste p : waste) {
			p.draw(this, new ArrayList<ColidableCircle>(cells), cam);
		}
		
		for (Cell c: cellsToRemove) {
			cells.remove(c);
		}
		
		for (Virus c: virusesToRemove) {
			viruses.remove(c);
		}
		
		if (framesSinceLastTick++ > frameRate) {
			framesSinceLastTick = 0;
			for (Cell c : cells) {
				c.tick();
			}
			for (Cell c: cellsToAdd) {
				cells.add(c);
			}
			cellsToAdd.clear();
		}
		this.hud.draw();
		//popMatrix();
	}
	
	public void mousePressed() {
		if (this.hud.click(mouseX, mouseY)) return;
		/*
		for (Cell c: cells) {
			if (c.isInside(mouseX, mouseY, cam)) {
				this.hud.show(c);
				return;
			}
		}
		this.hud.shown = null;*/
	}
	
	public void mouseWheel(MouseEvent event) {
		//magic
		cam.translate.x -= mouseX;
		cam.translate.y -= mouseY;
		float delta = (float)(event.getCount() < 0 ? 1.05 : event.getCount() > 0 ? 1.0/1.05 : 1.0);
		cam.zoom *= delta;
		cam.translate.x *= delta;
		cam.translate.y *= delta;
		cam.translate.x += mouseX;
		cam.translate.y += mouseY;
	}

	public void mouseDragged(MouseEvent event) {
		//if it's the right click button then pan
		if (event.getButton() == 39) {
			cam.translate.x += mouseX - pmouseX;
			cam.translate.y += mouseY - pmouseY;
		}
	}

	public void kill(Cell cell) {
		this.cellsToRemove.add(cell);
	}

	public void kill(Virus virus) {
		this.virusesToRemove .add(virus);
	}
}
