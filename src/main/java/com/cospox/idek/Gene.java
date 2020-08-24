package com.cospox.idek;

import processing.core.PApplet;
import processing.core.PVector;

public class Gene {
	private GeneType type;
	private static int width = 10;
	private boolean selected = false;
	public Gene(GeneType type) {
		this.type = type;
	}
	
	/*
	private int getActualColour(int col) {
		if (selected) {
			int r = (col & 0xFF0000) >> 16;
			int g = (col & 0x00FF00) >> 8;
			int b = (col & 0x0000FF) >> 0;
			r += 30;
			g += 30;
			b += 30;
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;
			return 0xFF << 24 | r << 16 | g << 8 | b;
		}
		return col;
	}*/
	
	public void draw(PApplet applet, PVector pos, float height, float angle) {
		applet.pushMatrix();
		applet.translate(pos.x, pos.y);
		applet.rotate(angle);
		applet.rectMode(PApplet.CENTER);
		applet.fill(type.getColour()[1]);
		applet.rect(0, 0, width, height);
		applet.fill(type.getColour()[0]);
		//applet.textSize(6);
		applet.rotate(PApplet.HALF_PI);
		//applet.text(type.toString(), -3, 2);
		applet.noFill();
		if (selected) {
			applet.rect(0, 0, width + 2, height + 2);
		}
		applet.popMatrix();
	}
	
	public GeneType getType() {
		return type;
	}
	
	public Gene copy() {
		return new Gene(this.type);
	}
	
	public void select() {
		selected = true;
	}
	
	public void deSelect() {
		selected = false;
	}
}
