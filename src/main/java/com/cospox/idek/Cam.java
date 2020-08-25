package com.cospox.idek;

import processing.core.PVector;

public class Cam {
	public PVector translate;
	public float zoom;
	public Cam() {
		translate = new PVector(0, 0);
		zoom = 1;
	}
}
