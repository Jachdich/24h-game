package com.cospox.idek;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PVector;

public class Virus extends ColidableCircle {
	public static final ArrayList<Gene> DEFAULT_GENOME = new ArrayList<Gene>(Arrays.asList(
			new Gene(GeneType.MAKE_VIRUS_SHELL),
			new Gene(GeneType.GENE_TO_RNA, new ArrayList<>(Arrays.asList(-4, 2)))
			));
			
			//TODO make this an actual virus
		
	private boolean dead = false;
	private ArrayList<Gene> genes = new ArrayList<Gene>();
	public Virus(PVector pos) {
		this.pos = pos;
		this.vel = PVector.random2D();
		this.rad = 15;
	}
	
	public void draw(Main applet, Cam cam) {
		applet.noFill();
		super.draw(applet, cam);
		
		float angle = 0;
		if (genes.size() <= 2) {
			for (Gene g : getGenes()) {
				g.draw(applet, pos,
						20 * PApplet.TWO_PI / getGenes().size(),
						angle, (getGenes().size() + 1), cam, 10);
				angle += PApplet.TWO_PI / getGenes().size();
			}
		} else {
			for (Gene g : getGenes()) {
				g.draw(applet, pos,
						20 * PApplet.TWO_PI / getGenes().size(),
						angle, getGenes().size(), cam, 10);
				angle += PApplet.TWO_PI / getGenes().size();
			}
		}
		
		if (dead) {
			applet.kill(this);
		}
	}

	public ArrayList<Gene> getGenes() {
		return genes;
	}

	public void die() {
		this.dead = true;
	}

	public void setGenes(ArrayList<Gene> genes2) {
		this.genes = genes2;
	}
	
	public long hash() {
		long out = 0;
		int idx = 0;
		for (Gene g : this.genes) {
			char ghash = g.hash();
			if (idx >= 8) {
				out = out ^ ghash << ((idx++ * 8) % 8);
			} else {
				out = out | ghash << (idx++ * 8);
			}
		}
		return out;
	}
}
