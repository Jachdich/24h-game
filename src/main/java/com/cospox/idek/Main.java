//4:58:00
//0:15:00
//0:30:00
//0:20:00
//0:20:00

//TODO
//Seperate screen for virus development?
//Initial configuration of cells
//energy
//waste removal
//virus render genes
//Better virus release mechanism
//Mutation
//Score
//Input data (numbers) to genes

package com.cospox.idek;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Main extends PApplet {
	public static final PVector boundry = new PVector(1000, 1000);
	public static final int waste_target = 48;
	public static final int food_target = 64;
	ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToAdd = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToRemove = new ArrayList<Cell>();
	ArrayList<Virus> viruses = new ArrayList<Virus>();
	private ArrayList<Virus> virusesToRemove = new ArrayList<Virus>();
	private ArrayList<Food> food = new ArrayList<Food>();
	private ArrayList<Waste> waste = new ArrayList<Waste>();
	private Cam cam;
	private HUD hud;
	private int framesSinceLastTick = 0;
	public int targetCells = 30;
	public boolean paused = false;
			
	public static void main(String[] args) {
		PApplet.main("com.cospox.idek.Main");
	}
	
	public void settings() {
		size(800, 800);
		smooth(10);
	}
	
	public void setup() {
		cam = new Cam();
		hud = new HUD(this);
		cells.add(new Cell(new PVector(100, 100), new PVector(100, 100), this));
		for (int i = 0; i < 100; i++)
			waste.add(new Waste(new PVector(random(boundry.x), random(boundry.y)), PVector.random2D()));

	}
	
	public void addCell(Cell c) {
		cellsToAdd.add(c);
	}
	
	//public void strokeWeight(float n) {
	//	
	//}
	
	public void draw() {
		background(255);
		noFill();
		stroke(0);
		rect(cam.translate.x, cam.translate.y, boundry.x * cam.zoom, boundry.y * cam.zoom);
		fill(0);
		text(frameRate, 10, 10);
		for (int i = food.size(); i < food_target; i++) {
			food.add(new Food(new PVector(random(boundry.x), random(boundry.y)), PVector.random2D()));
		}
		for (int i = waste.size(); i >= waste_target; i--) {
			waste.remove(new Random().nextInt(waste.size()));
		}
		//pushMatrix();
		//translate(this.translate.x, this.translate.y);
		//scale(zoom);
		for (Cell c: cells) {
			if (!paused) c.update(this, new ArrayList<ColidableCircle>(cells));
			c.draw(this, cam);
		}

		for (Virus v: viruses) {
			if (!paused) v.update(this, new ArrayList<ColidableCircle>(cells), true);
			v.draw(this, cam);
		}
		for (Food p : food) {
			if (!paused) p.update(this, new ArrayList<ColidableCircle>(cells), false);
			p.draw(this, cam);
		}
		for (Waste p : waste) {
			if (!paused) p.update(this, new ArrayList<ColidableCircle>(cells), true);
			p.draw(this, cam);
		}
		
		for (Cell c: cellsToRemove) {
			cells.remove(c);
		}
		
		for (Virus c: virusesToRemove) {
			viruses.remove(c);
		}
		
		if (paused) return;
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
		//viruses.add(new Virus(new PVector(mouseX, mouseY).sub(cam.translate).div(cam.zoom)));
	}
	
	public void keyPressed() {
		if (keyCode == 114) {
			paused = !paused;
		}
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
