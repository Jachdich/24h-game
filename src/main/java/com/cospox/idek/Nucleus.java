package com.cospox.idek;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Nucleus {
	private int headPos = 0;
	Cell parent;
	private ArrayList<Gene> genes = new ArrayList<Gene>();
	public Nucleus(Cell parent) {
		genes.add(new Gene(GeneType.NONE));
		genes.add(new Gene(GeneType.REPAIR_MEMBRANE));
		genes.add(new Gene(GeneType.DIVIDE_UNTIL));
		genes.add(new Gene(GeneType.PRODUCE_ATP));
		genes.add(new Gene(GeneType.COPY_GENE_INTO_RNA));
		genes.add(new Gene(GeneType.COPY_RNA_INTO_GENE));
		genes.add(new Gene(GeneType.INSERT_GENE_INTO_GENOME));
		genes.add(new Gene(GeneType.NONE));
		genes.add(new Gene(GeneType.NONE));
		genes.add(new Gene(GeneType.REPAIR_MEMBRANE));
		this.parent = parent;

	}
	
	public void tick() {
		genes.get(headPos).select();
		if (headPos == 0) {
			genes.get(genes.size() - 1).deSelect();
		} else {
			genes.get(headPos - 1).deSelect();
		}
		
		switch (genes.get(headPos).getType()) {
		case NONE: break;
		case REPAIR_MEMBRANE:
			this.parent.membraneHealth = 
			(this.parent.membraneHealth + (10 - this.parent.membraneHealth) * 0.8f) % 10;
			break;
		case DIVIDE: this.parent.divide(false); break;
		case DIVIDE_UNTIL: this.parent.divide(true); break;
		case COPY_GENE_INTO_RNA:
			break;
		case COPY_RNA_INTO_GENE:
			break;
		case INSERT_GENE_INTO_GENOME:
			break;
		case PRODUCE_ATP:
			break;
		default:
			break;
		}
		
		headPos += 1;
		headPos %= genes.size();
	}
	
	public void draw(PApplet applet, PVector pos, Cam cam) {
		applet.circle(pos.x * cam.zoom + cam.translate.x, pos.y * cam.zoom + cam.translate.y, 32 * cam.zoom);
		float angle = 0;
		for (Gene g : genes) {
			g.draw(applet, PVector.add(pos, PVector.fromAngle(angle).mult(20)), 20 * PApplet.TWO_PI / genes.size() / 1.1f, angle, cam);
			angle += PApplet.TWO_PI / genes.size();
		}
	}
	
	public Nucleus copy() {
		Nucleus n = new Nucleus(parent);
		n.genes = new ArrayList<Gene>();
		for (Gene g : this.genes) {
			n.genes.add(g.copy());
		}
		n.headPos = headPos;
		return n;
	}

	public Nucleus mutate() {
		Nucleus n = this.copy();
		for (Gene g: n.genes) {
			//TODO mutate genes
		}
		return n;
	}

}
