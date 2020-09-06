package com.cospox.idek;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Nucleus {
	public static final ArrayList<Gene> DEFAULT_GENOME = new ArrayList<Gene>(Arrays.asList(
			new Gene(GeneType.DIVIDE_UNTIL),
			new Gene(GeneType.PRODUCE_ATP),
			new Gene(GeneType.REMOVE_WASTE),
			new Gene(GeneType.REPAIR_MEMBRANE),
			new Gene(GeneType.GENE_TO_RNA, new ArrayList<>(Arrays.asList(-4, 2))),
			new Gene(GeneType.RNA_TO_GENE, new ArrayList<>(Arrays.asList(-5, 1))),
			new Gene(GeneType.REPAIR_MEMBRANE)));
	
	private int headPos = 0;
	public static float mutationChance = 1.0f;
	Cell parent;
	private ArrayList<Gene> genes = new ArrayList<Gene>();
	public Nucleus(Cell parent) {
//		getGenes().add(new Gene(GeneType.DIVIDE_UNTIL));
//		getGenes().add(new Gene(GeneType.PRODUCE_ATP));
//		getGenes().add(new Gene(GeneType.REMOVE_WASTE));
//		getGenes().add(new Gene(GeneType.REPAIR_MEMBRANE));
//		getGenes().add(new Gene(GeneType.GENE_TO_RNA, new ArrayList<>(Arrays.asList(-4, 2))));
//		getGenes().add(new Gene(GeneType.RNA_TO_GENE, new ArrayList<>(Arrays.asList(-5, 1))));
//		getGenes().add(new Gene(GeneType.REPAIR_MEMBRANE));
		this.parent = parent;

	}
	
	public void tick() {
		if (headPos >= getGenes().size()) headPos = 0;
		getGenes().get(headPos).select();
		getGenes().get(headPos).health -= Main.rand.nextFloat() * 2;
		if (headPos == 0) {
			getGenes().get(getGenes().size() - 1).deSelect();
		} else {
			getGenes().get(headPos - 1).deSelect();
		}
		
		this.parent.energy -= 0.05f;
		switch (getGenes().get(headPos).getType()) {
		case NONE: break;
		case REPAIR_MEMBRANE:
			this.parent.energy -= 0.15f;
			this.parent.membraneHealth = 
			(this.parent.membraneHealth + (10 - this.parent.membraneHealth) * 0.8f);
			break;
		case DIVIDE: this.parent.divide(false); break;
		case DIVIDE_UNTIL: this.parent.divide(true); break;
		case GENE_TO_RNA: {
			this.parent.energy -= 0.1f;
			ArrayList<Integer> data = getGenes().get(headPos).getData();
			int startPos = 0, endPos = 0;
			     if (data.size() == 0) { startPos = headPos; endPos = headPos; }
			else if (data.size() == 1) { startPos = headPos + data.get(0); endPos = headPos + data.get(0); }
			else if (data.size() >= 2) { startPos = headPos + data.get(0); endPos = headPos + data.get(1); }
			     
			ArrayList<Gene> newGenes = new ArrayList<Gene>();
			if (startPos >= getGenes().size() ||
				startPos < 0 ||
				endPos >= getGenes().size() ||
				endPos < 0) {
				break;
			}
			for (int i = startPos; i < endPos + 1; i++) {
				newGenes.add(getGenes().get(i).copy());
			}
			RNA rna = new RNA(newGenes, this.parent);
			rna.pos = this.parent.getPos().copy().add(PVector.random2D().mult(4));
			rna.vel = PVector.random2D();
			this.parent.rna.add(rna);
			break;
		}
		case RNA_TO_GENE:
			this.parent.energy -= 0.1f;
			
			if (this.parent.rna.size() > 0) {
				ArrayList<Integer> data = getGenes().get(headPos).getData();
				int startPos = 0, endPos = 0;
				     if (data.size() == 0) { startPos = 0; endPos = 0; }
				else if (data.size() == 1) { startPos = data.get(0); endPos = data.get(0); }
				else if (data.size() >= 2) { startPos = data.get(0); endPos = data.get(1); }
				int toRead = endPos - startPos;
				int i = 0;
				for (Gene g : this.parent.rna.get(this.parent.rna.size() - 1).genes) {
					int a = (headPos + startPos + i);
					if (a < 0) a = genes.size() + a;
					if (a < 0) a = 0;
					getGenes().set(a % genes.size(), g);
					if (toRead-- == 0) {
						break;
					}
					i++;
				}
				getGenes().get(headPos).select();
				this.parent.rna.remove(this.parent.rna.size() - 1);
			}
			break;
		case PRODUCE_ATP: {
			Food eat = null;
			for (Food f : this.parent.parent.food) {
				float dx = this.parent.getPos().x - f.pos.x;
				float dy = this.parent.getPos().y - f.pos.y;
				if (dx * dx + dy * dy < this.parent.rad * this.parent.rad) {
					eat = f;
					break;
				}
			}
			if (eat != null) {
				this.parent.parent.waste.add(new Waste(eat.pos.copy(), eat.vel.copy()));
				this.parent.parent.food.remove(eat);
				this.parent.energy += 2.0;
				if (this.parent.energy > 10) this.parent.energy = 10;
			}
			break;
		}
		case REMOVE_WASTE: {
			for (Waste w : this.parent.parent.waste) {
				float dx = w.pos.x - this.parent.pos.x;
				float dy = w.pos.y - this.parent.pos.y;
				if (dx * dx + dy * dy < this.parent.rad * this.parent.rad) {
					float angle = this.parent.parent.random(PConstants.TWO_PI);
					PVector pos = PVector.fromAngle(angle).mult(this.parent.rad * 1.3f).add(this.parent.pos);
					w.pos = pos;
					//TODO how to do this efficiently
					/*
					for (Cell c : parent.parent.cells) {
						float adx = pos.x - c.pos.x;
						float ady = pos.y - c.pos.y;
						if (adx * adx + ady * ady < c.rad * c.rad) {
							//fucking try again
						}
					}*/
					//break;
				}
			}
			break;
		}
		
		case MAKE_VIRUS_SHELL: {
			/*
			Virus v = new Virus(this.parent.pos.copy());
			if (this.parent.rna.size() > 0) {
				v.setGenes(this.parent.rna.get(this.parent.rna.size() - 1).genes);
			} else {
				v.setGenes(new ArrayList<Gene>());
			}
			this.parent.rna.remove(this.parent.rna.size() - 1);
			this.parent.parent.viruses.add(v);
			this.parent.parent.score += 150;*/
			this.parent.shells.add(new VirusShell(this.parent, this.parent.pos.copy()));
			break;
		}
			
		default:
			break;
		}
		
		headPos += 1;
		headPos %= getGenes().size();
	}
	
	public void draw(PApplet applet, PVector pos, Cam cam) {
		applet.noFill();
		applet.stroke(0);
		applet.circle(pos.x * cam.zoom + cam.translate.x, pos.y * cam.zoom + cam.translate.y, 32 * cam.zoom);
		float angle = 0;
		for (Gene g : getGenes()) {
			g.draw(applet, pos,
					20 * PApplet.TWO_PI / getGenes().size(),
					angle, getGenes().size(), cam);
			angle += PApplet.TWO_PI / getGenes().size();
		}
	}
	
	public Nucleus copy() {
		Nucleus n = new Nucleus(parent);
		n.setGenes(new ArrayList<Gene>());
		for (Gene g : this.getGenes()) {
			n.getGenes().add(g.copyWithHealth());
		}
		n.headPos = Main.rand.nextInt(n.getGenes().size());
		return n;
	}

	public Nucleus mutate() {
		Nucleus n = this.copy();
		ArrayList<Gene> temp = new ArrayList<Gene>(n.getGenes());
		ArrayList<Gene> genes = n.getGenes();
		genes.clear();
		for (Gene g: temp) {
			if (Main.rand.nextDouble() < 0.008 * mutationChance) {
				GeneType ty = GeneType.values()[Main.rand.nextInt(GeneType.values().length)];
				if (Main.rand.nextDouble() < 0.3) {
					int datalen = Main.rand.nextInt(4);
					ArrayList<Integer> data = new ArrayList<Integer>();
					for (int i = 0; i < datalen; i++) {
						data.add(Main.rand.nextInt(temp.size()));
					}
					
					genes.add(new Gene(ty, data));
				} else {
					genes.add(new Gene(ty));
				}
			} else {
				if (Main.rand.nextDouble() > 0.005 * mutationChance) {
					genes.add(g);
				}
			}
		}
		if (Main.rand.nextDouble() < 0.005 * mutationChance * temp.size()) {
			genes.add(Main.rand.nextInt(genes.size()),
					new Gene(GeneType.values()[Main.rand.nextInt(GeneType.values().length)]));
		}
		return n;
	}

	public void merge(ArrayList<Gene> genes) {
		this.getGenes().addAll(genes);
	}

	public ArrayList<Gene> getGenes() {
		return genes;
	}

	public void setGenes(ArrayList<Gene> genes) {
		this.genes = genes;
	}
	
	public void setGenesCopy(ArrayList<Gene> genes) {
		this.genes.clear();
		for (Gene g : genes) {
			this.genes.add(g.copy());
		}
	}

}
