package com.cospox.idek;

import processing.core.PApplet;
import processing.core.PVector;

public class HUD {
	private Main parent;
	private Virus virus = null;
	private Gene selectedGene = null;
	public HUD(Main parent) {
		this.parent = parent;
	}
	
	public void draw() {
		if (virus != null) {
			parent.noStroke();
			parent.fill(128);
			parent.rect(0, 0, 120 * 2 + 20 * 2, PApplet.max(virus.getGenes().size(), GeneType.values().length)
					* 30 + 30 * 3);
			int y = 30;
			for (Gene g : virus.getGenes()) {
				parent.fill(g.getType().getColour()[1]);
				if (g == this.selectedGene) {
					parent.stroke(g.getType().getColour()[0]);
					parent.strokeWeight(2);
				} else {
					parent.noStroke();
				}
				parent.rect(20, y, 120, 29);
				parent.fill(g.getType().getColour()[0]);
				parent.text(g.getType().name(), 20, y + 19);
				y += 30;
			}
			parent.stroke(0);
			parent.strokeWeight(3);
			//plus and minus
			parent.line(30, y + 10, 30, y + 30);
			parent.line(20, y + 20, 40, y + 20);
			parent.line(50, y + 20, 70, y + 20);
			parent.strokeWeight(1);
			y += 40;
			parent.fill(0);
			parent.text("Release", 20, y + 10);
			
			//gene type menu
			y = 30;
			parent.noStroke();
			for (GeneType g : GeneType.values()) {
				parent.fill(g.getColour()[1]);
				parent.rect(20 + 120 + 10, y, 120, 30);
				parent.fill(g.getColour()[0]);
				parent.text(g.name(), 20 + 120 + 10, y + 19);
				y += 30;
			}
			
		}
		
	}

	public boolean click(int mouseX, int mouseY) {
		if (mouseX < 10 && mouseY < 10) {
			this.virus = new Virus(new PVector(0,0));
			return true;
		}
		if (this.virus == null) return false;
		if (mouseX > 20 + 120 + 10 && mouseX < 20 + 120 * 2 + 10 && this.selectedGene != null) {
			if (mouseY > 30) {
				int idx = (mouseY - 30) / 30;
				if (idx < GeneType.values().length) {
					this.selectedGene.setType(GeneType.values()[idx]);
					return true;
				}
			}
		}
		if (mouseX > 20 && mouseX < 20 + 120) {
			if (mouseY > 30) {
				int idx = (mouseY - 30) / 30;
				if (idx < this.virus.getGenes().size()) {
					this.selectedGene = this.virus.getGenes().get(idx);
					return true;
				}
			}
		}
		int y = virus.getGenes().size() * 30 + 30;
		if (mouseY > y && mouseY < y + 30) {
			if (mouseX > 20 && mouseX < 40) {
				//+
				this.virus.getGenes().add(new Gene(GeneType.NONE));
				this.selectedGene = this.virus.getGenes().get(this.virus.getGenes().size() - 1);
				return true;
			}
			if (mouseX > 50 && mouseX < 70) {
				//-
				this.virus.getGenes().remove(this.virus.getGenes().size() - 1);
				return true;
				
			}
		} else if (mouseY > y + 40 && mouseY < y + 60 && mouseX > 20 && mouseX < 20 + 120) {
			//release
			this.parent.viruses.add(this.virus);
			this.virus = null;
			return true;
		}
		return false;
	}
}
