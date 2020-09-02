package com.cospox.idek;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Gene {
	private GeneType type;
	private static int width = 5;
	float health = 10.0f;
	private boolean selected = false;
	private int[] data = {};
	public Gene(GeneType type) {
		this.setType(type);
	}
	
	public Gene(GeneType type, int[] data) {
		this.data = data;
		this.setType(type);
	}
	
	public int[] getData() {
		return this.data;
	}
	
	
	private int getActualColour(int col) {
		int r = (col & 0xFF0000) >> 16;
		int g = (col & 0x00FF00) >> 8;
		int b = (col & 0x0000FF) >> 0;
		return 0xFF << 24 |
				(int)(r  * (health / 10)) << 16 |
				(int)(g  * (health / 10)) << 8 |
				(int)(b  * (health / 10));
	}
	
	public Gene(GeneType type, RNA rna) {
		this.setType(type);
	}
	
	void draw_thing(PApplet applet, float r1, float r2, float angle, int n) {
		  float s = PConstants.TWO_PI / n;
		  applet.beginShape();
		  float x = r1 * PApplet.cos(angle);
		  float y = r1 * PApplet.sin(angle);
		  applet.vertex(x, y);
		  x = r1 * PApplet.cos(angle + s);
		  y = r1 * PApplet.sin(angle + s);
		  applet.vertex(x, y);
		  x = r2 * PApplet.cos(angle + s);
		  y = r2 * PApplet.sin(angle + s);
		  applet.vertex(x, y);
		  x = r2 * PApplet.cos(angle);
		  y = r2 * PApplet.sin(angle);
		  applet.vertex(x, y);
		  applet.endShape(PConstants.CLOSE);
	}

	public void draw(PApplet applet, PVector pos, float height, float angle, int n, Cam cam) {
		applet.pushMatrix();
		applet.translate(pos.x * cam.zoom + cam.translate.x, pos.y * cam.zoom + cam.translate.y);
		applet.fill(getActualColour(getType().getColour()[1]));
		if (this.selected) {
			applet.stroke(255, 0, 0);
			applet.strokeWeight(cam.zoom);
		}
		for (int i = 0; i < data.length + 1; i++) {
			draw_thing(applet, (20 - width + width * i) * cam.zoom, (20 + width * i) * cam.zoom, angle, n);
		}
		applet.stroke(0);
		applet.strokeWeight(1);
		applet.popMatrix();
	}
	
	public GeneType getType() {
		return type;
	}
	
	public Gene copy() {
		return new Gene(this.getType(), this.data.clone());
	}
	
	public void select() {
		selected = true;
	}
	
	public void deSelect() {
		selected = false;
	}

	public void setType(GeneType type) {
		this.type = type;
	}
}
