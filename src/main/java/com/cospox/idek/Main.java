//1:28:00
//0:15:00
//0:05:00 * 2
//0:15:00
//0:10:00

package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Main extends PApplet {
	ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToAdd = new ArrayList<Cell>();
	private ArrayList<Virus> viruses = new ArrayList<Virus>();
	private ArrayList<Food> food = new ArrayList<Food>();
	private ArrayList<Waste> waste = new ArrayList<Waste>();
	private PVector translate = new PVector(0, 0);
	private float zoom = 1;
	private int framesSinceLastTick = 0;
			
	public static void main(String[] args) {
		PApplet.main("com.cospox.idek.Main");
	}
	
	//public void circle(float x, float y, float d) {
	//	
	//}
	
	public void settings() {
		size(500, 500, P2D);
		smooth(10);
	}
	
	public void setup() {
		cells.add(new Cell(new PVector(10, 10), this));
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
		translate(this.translate.x, this.translate.y);
		scale(zoom);
		for (Cell c: cells) {
			c.draw(this);
		}

		for (Virus v: viruses) {
			v.draw(this, new ArrayList<ColidableCircle>(cells));
		}
		for (Food p : food) {
			p.draw(this, new ArrayList<ColidableCircle>(cells));
		}
		for (Waste p : waste) {
			p.draw(this, new ArrayList<ColidableCircle>(cells));
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
		viruses.add(new Virus(new PVector(mouseX, mouseY).sub(translate).div(zoom)));
	}
	
	public void mouseWheel(MouseEvent event) {
		//magic
		this.translate.x -= mouseX;
		this.translate.y -= mouseY;
		float delta = (float)(event.getCount() < 0 ? 1.05 : event.getCount() > 0 ? 1.0/1.05 : 1.0);
		this.zoom *= delta;
		this.translate.x *= delta;
		this.translate.y *= delta;
		this.translate.x += mouseX;
		this.translate.y += mouseY;
	}

	public void mouseDragged(MouseEvent event) {
		//if it's the right click button then pan
		if (event.getButton() == 39) {
			this.translate.x += mouseX - pmouseX;
			this.translate.y += mouseY - pmouseY;
		}
	}
}
