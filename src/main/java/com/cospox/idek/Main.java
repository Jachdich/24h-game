//10:40:00
//02:10:00
//00:10:00
//00:40:00

//TODO
//settings?
//tutorial
//Better title screen
//Score for both gamemodes

//BUGS
//Waste can get trapped "inside" a cell wall causing the cell to die
//Is that a bug??

package com.cospox.idek;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Main extends PApplet {
	public static final PVector boundry = new PVector(700, 700);
	public static Random rand = new Random();
	public static final int waste_target = 32;
	public static final int food_target = 128;
	ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToAdd = new ArrayList<Cell>();
	private ArrayList<Cell> cellsToRemove = new ArrayList<Cell>();
	ArrayList<Virus> viruses = new ArrayList<Virus>();
	private ArrayList<Virus> virusesToRemove = new ArrayList<Virus>();
	ArrayList<Food> food = new ArrayList<Food>();
	ArrayList<Waste> waste = new ArrayList<Waste>();
	private Cam cam;
	private HUD hud;
	private int framesSinceLastTick = 0;
	public int targetCells = 24;
	public boolean paused = false;
	private boolean mainMenu = true;
	boolean virusCreate = false;
	boolean cellCreate = false;
	boolean cellView = false;
	boolean offenceMenu = false;
	boolean defenceMenu = false;
	
	boolean offenceTutorial = false;
	boolean defenceTutorial = false;
	int slide = 0;
	PImage[] offenceSlides;
	PImage[] defenceSlides;
	
	boolean offenceMode = false;
	public int score = 0;
			
	public static void main(String[] args) {
		PApplet.main("com.cospox.idek.Main");
	}
	
	public void settings() {
		size(800, 800, P3D);
	}
	
	public void setup() {
		defenceSlides = new PImage[10];
		offenceSlides = new PImage[10];
		defenceSlides[0] = loadImage("defence0.png");
		defenceSlides[1] = loadImage("defence1.png");
		defenceSlides[2] = loadImage("defence2.png");
		defenceSlides[3] = loadImage("defence3.png");
		defenceSlides[4] = loadImage("defence4.png");
		defenceSlides[5] = loadImage("defence5.png");
		defenceSlides[6] = loadImage("defence6.png");
		defenceSlides[7] = loadImage("defence7.png");
		defenceSlides[8] = loadImage("defence8.png");
		defenceSlides[9] = loadImage("defence9.png");
		
		
		smooth();
		cam = new Cam();
		hud = new HUD(this);
		//placeInitialCells(new ArrayList<Gene>());
		for (int i = 0; i < 100; i++)
			waste.add(new Waste(new PVector(random(boundry.x), random(boundry.y)), PVector.random2D().mult(0.8f)));
	}
	
	public void addCell(Cell c) {
		cellsToAdd.add(c);
	}
	
	public void placeInitialCells(ArrayList<Gene> genes) {
		PVector cellPos[] = {
				new PVector(100, 100),
				new PVector(100, 200),
				new PVector(200, 100),
				new PVector(200, 200),
				
				new PVector(boundry.x - 100, 100),
				new PVector(boundry.x - 100, 200),
				new PVector(boundry.x - 200, 100),
				new PVector(boundry.x - 200, 200),
				
				new PVector(100, boundry.y - 100),
				new PVector(100, boundry.y - 200),
				new PVector(200, boundry.y - 100),
				new PVector(200, boundry.y - 200),
				
				new PVector(boundry.x - 100, boundry.y - 100),
				new PVector(boundry.x - 100, boundry.y - 200),
				new PVector(boundry.x - 200, boundry.y - 100),
				new PVector(boundry.x - 200, boundry.y - 200),
		};
		for (PVector p : cellPos) {
			Cell c = new Cell(p, p, this);
			c.getNucleus().setGenesCopy(genes);
			this.cells.add(c);
		}
	}
	
	//public void strokeWeight(float n) {
	//	
	//}
	
	public void draw() {
		if (cellView) drawCellView();
		if (mainMenu) drawATitleScreen("Defence mode", "Offence mode");
		if (offenceMenu) drawATitleScreen("Make a virus", "How to play");
		if (defenceMenu) drawATitleScreen("Make a cell", "How to play");
		if (virusCreate) drawVirusCreate();
		if (cellCreate) drawVirusCreate();
		if (defenceTutorial) {
			if (slide >= defenceSlides.length) {
				defenceTutorial = false;
				defenceMenu = true;
				return;
			}
			image(defenceSlides[slide], 0, 0);
		}
	}
	
	private void drawATitleScreen(String buttonA, String buttonB) {
		background(255);
		stroke(0);
		noFill();
		rect(width/2 - width/2*0.8f - 10, 200, width/2 * 0.8f, 100);
		rect(width/2 + 10, 200, width/2 * 0.8f, 100);
		textSize(50);
		text(buttonA, width/2 - width/2*0.8f - 10 + (width/2 * 0.8f / 2 - textWidth(buttonA)/2), 250);
		text(buttonB, width/2 + 10 + (width/2 * 0.8f / 2 - textWidth(buttonB)/2), 250);
		fill(0);
		textSize(70);
		text("Unnamed Virus Game", width / 2 - textWidth("Unnamed Virus game") / 2, 100);
		textSize(11.5f);
	}
	
	private void drawVirusCreate() {
		background(200);
		this.hud.draw();
	}
	
	private boolean clickInside(int mx, int my, float x, float y, float w, float h) {
		return (mx > x &&
			mx < x + w &&
			my > y &&
			my < y + h);		
	}
	
	private void drawCellView() {
		background(255);
		noFill();
		stroke(0);
		rect(cam.translate.x, cam.translate.y, boundry.x * cam.zoom, boundry.y * cam.zoom);
		fill(0);
		text(frameRate, 10, 10);
		text(score, 70, 10);
		//if (frameRate < 100000) return;
		for (int i = food.size(); i < food_target; i++) {
			food.add(new Food(new PVector(random(boundry.x), random(boundry.y)), PVector.random2D().mult(0.8f)));
		}
		
		//TODO ??
		int pos = 0;
		while (waste.size() >= waste_target) {
			Waste w = waste.get(pos);
			boolean remove = true;
			for (Cell c : cells) {
				float dx = w.pos.x - c.pos.x;
				float dy = w.pos.y - c.pos.y;
				if (dx * dx + dy * dy < c.rad * c.rad) {
					remove = false;
					break;
				}
			}
			if (remove) {
				waste.remove(w);
			}
			if (++pos >= waste.size()) break;
		}
		for (Cell c: cells) {
			if (!paused) c.update(this, new ArrayList<ColidableCircle>(cells));
			c.draw(this, cam);
		}

		for (Virus v: viruses) {
			if (!paused) v.update(this, new ArrayList<ColidableCircle>(cells), true);
			v.draw(this, cam);
		}
		for (Food p : food) {
			if (!paused) p.update(this, new ArrayList<ColidableCircle>(cells), false);
			p.draw(this, cam);
		}
		for (Waste p : waste) {
			if (!paused) p.update(this, new ArrayList<ColidableCircle>(cells), true);
			p.draw(this, cam);
		}
		
		for (Cell c: cellsToRemove) {
			cells.remove(c);
		}
		
		for (Virus c: virusesToRemove) {
			viruses.remove(c);
		}
		
		if (paused) return;
		if (framesSinceLastTick++ > frameRate) {
			framesSinceLastTick = 0;
			for (Cell c : cells) {
				c.tick();
			}
			for (Cell c: cellsToAdd) {
				cells.add(c);
			}
			cellsToAdd.clear();
		}
//		this.hud.draw();
	}
	
	public void mousePressed() {
		if (defenceTutorial || offenceTutorial) slide++;
		if (this.hud.click(mouseX, mouseY)) return;
		if (clickInside(mouseX, mouseY, width/2 - width/2*0.8f - 10, 200, width/2 * 0.8f, 100)) {
			//create virus
			//temporarily, show cell view
			//cellView = true;
			//virusCreate = true;
			if (mainMenu) {
				mainMenu = false;
				defenceMenu = true;
			}
			else if (offenceMenu) {
				offenceMenu = false;
				virusCreate = true;
				this.hud.virus = new Virus(new PVector(Main.boundry.x / 2, Main.boundry.y / 2));
			}
			else if (defenceMenu) {
				defenceMenu = false;
				cellCreate = true;
				this.hud.cell = new Cell(new PVector(Main.boundry.x / 2, Main.boundry.y / 2), new PVector(Main.boundry.x / 2, Main.boundry.y / 2), this);
			}
			
			//mainMenu = false;
		}
		if (clickInside(mouseX, mouseY, width/2 + 10, 200, width/2 * 0.8f, 100)) {
			if (mainMenu) {
				mainMenu = false;
				offenceMenu = true;
			}
			else if (offenceMenu) {
				offenceTutorial = true;
				offenceMenu = false;
			}
			else if (defenceMenu) {
				defenceTutorial = true;
				defenceMenu = false;
			}
		}
		/*
		for (Cell c: cells) {
			if (c.isInside(mouseX, mouseY, cam)) {
				this.hud.show(c);
				return;
			}
		}
		this.hud.shown = null;*/
		//viruses.add(new Virus(new PVector(mouseX, mouseY).sub(cam.translate).div(cam.zoom)));
	}
	
	public void keyPressed() {
		if (keyCode == 114) {
			paused = !paused;
		}
		this.hud.keyPressed(keyCode);
	}
	
	public void mouseWheel(MouseEvent event) {
		//magic
		cam.translate.x -= mouseX;
		cam.translate.y -= mouseY;
		float delta = (float)(event.getCount() < 0 ? 1.05 : event.getCount() > 0 ? 1.0/1.05 : 1.0);
		cam.zoom *= delta;
		cam.translate.x *= delta;
		cam.translate.y *= delta;
		cam.translate.x += mouseX;
		cam.translate.y += mouseY;
	}

	public void mouseDragged(MouseEvent event) {
		//if it's the right click button then pan
		if (event.getButton() == 39) {
			cam.translate.x += mouseX - pmouseX;
			cam.translate.y += mouseY - pmouseY;
		}
	}

	public void kill(Cell cell) {
		this.cellsToRemove.add(cell);
	}

	public void kill(Virus virus) {
		this.virusesToRemove .add(virus);
	}
}
