//1:28:00
//0:15:00
//0:05:00 * 2
//0:15:00
//0:10:00
//0:40:00

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
	private ArrayList<Food> food = new ArrayList<Food>();
	private ArrayList<Waste> waste = new ArrayList<Waste>();
	private Cam cam;
	private int framesSinceLastTick = 0;
	public int targetCells = 30;
			
	public static void main(String[] args) {
		PApplet.main("com.cospox.idek.Main");
	}
	
	//public void circle(float x, float y, float d) {
	//	
	//}
	
	public void settings() {
		size(500, 500);
		smooth(10);
	}
	
	public void setup() {
		cam = new Cam();
		cells.add(new Cell(new PVector(10, 10), new PVector(10, 10), this));
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
			c.draw(this, cam);
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
		
		if (framesSinceLastTick++ > 60) {
			framesSinceLastTick = 0;
			for (Cell c : cells) {
				c.tick();
			}
			for (Cell c: cellsToAdd) {
				cells.add(c);
			}
			cellsToAdd.clear();
		}
		//popMatrix();
	}
	
	public void mousePressed() {
		//viruses.add(new Virus(new PVector(mouseX, mouseY).sub(cam.translate).div(cam.zoom)));
		waste.add(new Waste(new PVector(mouseX, mouseY).sub(cam.translate).div(cam.zoom), PVector.random2D()));
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
}
