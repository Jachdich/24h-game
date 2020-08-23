//20:00
//17:00
//51:00

package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Main extends PApplet {
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToAdd = new ArrayList<Cell>();
	private PVector translate = new PVector(0, 0);
	private float zoom = 1;
	private int framesSinceLastTick = 0;
	public static void main(String[] args) {
		PApplet.main("com.cospox.idek.Main");
	}
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		cells.add(new Cell(new PVector(10, 10), this));
	}
	
	public void addCell(Cell c) {
		cellsToAdd.add(c);
	}
	
	public void draw() {
		background(255);
		translate(this.translate.x, this.translate.y);
		scale(zoom);
		for (Cell c: cells) {
			c.draw(this);
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
