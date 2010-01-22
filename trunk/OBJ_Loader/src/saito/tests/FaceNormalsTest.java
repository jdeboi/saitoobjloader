package saito.tests;

import saito.objloader.*;

import processing.core.PApplet;
import processing.core.PVector;

public class FaceNormalsTest extends PApplet {
	OBJModel model;

	float rotX;
	float rotY;

	int faceCount;
	int faceIndex;
	float normLength = -25;
	float normDir = 1;

	@Override
	public void setup() {
		size(600, 600, P3D);
		
		model = new OBJModel(this);
		model.load("map_ground_path_s.obj");

		model.scale(150);
		model.translateToCenter();

		faceCount = model.getIndexCountInSegment(0);
	}

	@Override
	public void draw() {
		background(32);
		lights();

		pushMatrix();
		translate(width / 2, height / 2, 0);
		rotateX(rotY);
		rotateY(rotX);

		for (int j = 0; j < model.getSegmentCount(); j++) {
			Segment segment = model.getSegment(j);
			Face[] faces = segment.getFaces();

			// draw faces
			noStroke();
			fill(196);
			beginShape(QUADS);
			for (int i = 0; i < faces.length; i++) {
				Face f = faces[i];
				PVector[] vs = f.getVertices();

				for (int k = 0; k < vs.length; k++)
					vertex(vs[k].x, vs[k].y, vs[k].z);
			}
			endShape();

			// draw face normals
			for (int i = 0; i < faces.length; i++) {
				PVector v = faces[i].getCenter();
				PVector n = faces[i].getNormal();

				stroke(255, 0, 255);
				beginShape(LINES);
				vertex(v.x, v.y, v.z);
				vertex(v.x + (n.x * normLength), v.y + (n.y * normLength), v.z + (n.z * normLength));
				endShape();
			}
		}
		popMatrix();
	}

	@Override
	public void mouseDragged() {
		rotX += (mouseX - pmouseX) * 0.01;
		rotY -= (mouseY - pmouseY) * 0.01;
	}
}
