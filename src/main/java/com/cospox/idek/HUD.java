package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class HUD {
	private Main parent;
	Virus virus = null;
	private Gene selectedGene = null;
	private Integer selectedData = null;
	private String textBuffer = "";
	public Cell cell;
	private boolean negetive = false;
	public HUD(Main parent) {
		this.parent = parent;
	}
	
	public void draw() {
		if (virus != null || cell != null) {
			parent.noStroke();
			//parent.fill(128);
			//parent.rect(0, 0, 120 * 3 + 20 * 2, PApplet.max(virus.getGenes().size(), GeneType.values().length)
			//		* 30 + 30 * 3);
			
			int y = 60;
			parent.fill(0);
			parent.textSize(18);
			ArrayList<Gene> genes = null;
			if (parent.cellCreate) {
				parent.text("Create Cell", 10, 15);
				parent.textSize(11.5f);
				genes = Virus.DEFAULT_GENOME;
				parent.text("This is the virus's genome", 40 + 120*3, 30);
				parent.text("you'll be up against", 40 + 120*3, 42);
			} else if (parent.virusCreate) {
				parent.text("Create Virus", 10, 15);
				parent.textSize(11.5f);
				genes = Nucleus.DEFAULT_GENOME;
				parent.text("This is the cell's genome", 40 + 120*3, 30);
				parent.text("you'll be up against", 40 + 120*3, 42);
			}
			for (Gene g : genes) {
				parent.fill(g.getType().getColour()[1]);
				parent.rect(20 * 2 + 120 * 3, y, 120, 30);
				parent.fill(g.getType().getColour()[0]);
				parent.text(g.getType().humanName(), 20 * 2 + 120 * 3, y + 19);
				y += 30;
			}
			
			y = 30;
			for (Gene g : getGenes()) {
				parent.fill(g.getType().getColour()[1]);
				if (g == this.selectedGene) {
					parent.stroke(g.getType().getColour()[0]);
					parent.strokeWeight(2);
				} else {
					parent.noStroke();
				}
				parent.rect(20, y, 120, 29);
				parent.noStroke();
				int f = 121;
				int idx = 0;
				for (int x : g.getData()) {
					if (this.selectedData != null && this.selectedData.equals(idx) && g == this.selectedGene) {
						parent.stroke(g.getType().getColour()[0]);
						parent.strokeWeight(2);
					} else {
						parent.noStroke();
					}
					parent.fill(g.getType().getColour()[1]);
					parent.rect(20 + f, y, 29, 29);
					parent.fill(g.getType().getColour()[0]);
					parent.text(Integer.toString(x), 24 + f, y + 20);
					f += 30;
					idx += 1;
				}
				parent.fill(g.getType().getColour()[0]);
				parent.text(g.getType().humanName(), 20, y + 19);
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
			y += 15;
			parent.text("Add Data", 20, y + 10);
			
			//gene type menu
			y = 30;
			parent.noStroke();
			for (GeneType g : GeneType.values()) {
				parent.fill(g.getColour()[1]);
				parent.rect(20 + 120 * 2, y, 120, 30);
				parent.fill(g.getColour()[0]);
				parent.text(g.humanName(), 20 + 120 * 2, y + 19);
				y += 30;
			}
			
		}
		
	}
	
	private ArrayList<Gene> getGenes() {
		if (this.virus != null) return this.virus.getGenes();
		if (this.cell != null) return this.cell.getNucleus().getGenes();
		return null;
	}
	
	public void keyPressed(int keyCode) {
		if (keyCode == PConstants.BACKSPACE) {
			if (this.textBuffer.length() > 0) {
				this.textBuffer = this.textBuffer.substring(0, this.textBuffer.length() - 1);
			}
		} else if (keyCode >= '0' && keyCode <= '9') {
			this.textBuffer = this.textBuffer + String.valueOf((char)keyCode);
		}
		if (keyCode == '-') {
			this.negetive = !this.negetive;
		}
		if (this.selectedGene != null && this.selectedData != null) {
			try {
				int x = Integer.parseInt(textBuffer);
				if (negetive) x = -x;
				this.selectedGene.getData().set(this.selectedData, x);
			} catch (NumberFormatException e) {
				this.selectedGene.getData().set(this.selectedData, 0);
			}
		}
	}

	public boolean click(int mouseX, int mouseY) {
		if (this.virus == null && this.cell == null) return false;
		if (mouseX > 20 + 120 * 2 && mouseX < (20 + 120 * 3) && this.selectedGene != null) {
			if (mouseY > 30) {
				int idx = (mouseY - 30) / 30;
				if (idx < GeneType.values().length) {
					this.selectedGene.setType(GeneType.values()[idx]);
					return true;
				}
			}
		}
		if (mouseX > 20 && mouseX < 20 + 120 + 30 * 3) {
			if (mouseY > 30) {
				int idx = (mouseY - 30) / 30;
				if (idx < this.getGenes().size()) {
					this.selectedGene = this.getGenes().get(idx);
					if (mouseX > 20 + 120) {
						//clicked on data
						int dataIdx = (mouseX - 20 - 120) / 30;
						if (dataIdx < this.selectedGene.getData().size()) {
							this.selectedData = dataIdx;
							this.textBuffer = Integer.toString(this.selectedGene.getData().get(dataIdx));
							this.negetive = false;
						} else this.selectedData = null;
					} else this.selectedData = null;
					return true;
				}
			}
		}
		int y = getGenes().size() * 30 + 30;
		if (mouseY > y && mouseY < y + 30) {
			if (mouseX > 20 && mouseX < 40) {
				//+
				this.getGenes().add(new Gene(GeneType.NONE));
				this.selectedGene = this.getGenes().get(this.getGenes().size() - 1);
				this.negetive = false;
				return true;
			}
			if (mouseX > 50 && mouseX < 70) {
				//-
				this.getGenes().remove(this.getGenes().size() - 1);
				return true;
				
			}
		} else if (mouseY > y + 40 && mouseY < y + 55 && mouseX > 20 && mouseX < 20 + 120) {
			//release
			
			if (this.virus != null) {
				this.parent.offenceMode = true;
				this.parent.viruses.add(this.virus);
				this.virus = null;
				this.parent.placeInitialCells(Nucleus.DEFAULT_GENOME);
			} else if (this.cell != null) {
				this.parent.placeInitialCells(this.cell.getNucleus().getGenes());
				this.parent.offenceMode = false;
				this.cell = null;
			}
			this.parent.cellView = true;
			this.parent.virusCreate = false;
			this.parent.cellCreate = false;
			return true;
		} else if (mouseY > y + 40 + 15 && mouseY < y + 40 + 15*2 && mouseX > 20 && mouseX < 20 + 120) {
			//add data
			if (this.selectedGene == null) return true;
			if (this.selectedGene.getData().size() >= 3) return true;
			this.selectedGene.getData().add(0);
			this.selectedData = this.selectedGene.getData().size() - 1;
			this.textBuffer = "0";
			return true;
		}
		return false;
	}
}
