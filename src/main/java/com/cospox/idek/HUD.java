package com.cospox.idek;

import processing.core.PVector;

public class HUD {
	private Main parent;
	private Virus virus = null;
	public HUD(Main parent) {
		this.parent = parent;
	}
	
	public void draw() {
		if (virus != null) {
			int y = 0;
			for (Gene g : virus.getGenes()) {
				parent.fill(g.getType().getColour()[1]);
				parent.rect(0, y, 60, 30);
				y += 30;
			}
		}
	}

	public boolean click(int mouseX, int mouseY) {
		if (mouseX == 0 && mouseY == 0) {
			this.virus = new Virus(new PVector(0,0)); 
			return true;
		}
		return false;
	}
}
