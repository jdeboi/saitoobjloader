package saito.test;

import saito.objloader.*;

import processing.core.PApplet;
import processing.core.PVector;

public class FaceNormalsTest extends PApplet {
	OBJModel model;

	float rotX;
	float rotY;

	int faceCount;
	int face;
	float normLength = 0;
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

		normLength += normDir;

		if (normLength < -30 || normLength > 30) {

			normDir = -normDir;

		}

		for (int j = 0; j < model.getSegmentCount(); j++) {
			faceCount = model.getIndexCountInSegment(j);

			if (frameCount % 2 == 0)
				face = (face + 1) % faceCount;

			int[] vertIndex = model.getVertexIndicesInSegment(j, face);
			int[] normIndex = model.getNormalIndicesInSegment(j, face);

			// the original face
			noStroke();
			fill(196);
			beginShape(QUADS);
			for (int i = 0; i < vertIndex.length; i++) {
				PVector v = model.getVertex(vertIndex[i]);
				vertex(v.x, v.y, v.z);
			}
			endShape();

			// the normal
			stroke(255, 0, 255);
			beginShape(LINES);
			for (int i = 0; i < vertIndex.length; i++) {
				PVector v = model.getVertex(vertIndex[i]);
				PVector n = model.getNormal(normIndex[i]);

				vertex(v.x, v.y, v.z);
				vertex(v.x + (n.x * normLength), v.y + (n.y * normLength), v.z + (n.z * normLength));
			}
			endShape();

			// the face projected along the normal
			fill(196, 0, 196, 195);
			noStroke();

			beginShape(QUADS);

			for (int i = 0; i < vertIndex.length; i++) {
				PVector v = model.getVertex(vertIndex[i]);
				PVector n = model.getNormal(normIndex[i]);

				vertex(v.x + (n.x * normLength), v.y + (n.y * normLength), v.z + (n.z * normLength));
			}
			endShape();
		}
		popMatrix();
	}

	@Override
	public void mouseDragged() {
		rotX += (mouseX - pmouseX) * 0.01;
		rotY -= (mouseY - pmouseY) * 0.01;
	}
}
