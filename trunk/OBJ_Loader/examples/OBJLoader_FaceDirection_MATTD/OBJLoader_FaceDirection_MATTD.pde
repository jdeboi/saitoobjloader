import saito.objloader.*;

OBJModel model;

float rotX;
float rotY;

float normLength = -25;

PVector pos;

void setup() {
  size(600, 600, P3D);

  model = new OBJModel(this);
  model.load("cube_sphere_test.obj");

  model.scale(3);
  model.translateToCenter();

  pos = new PVector();
}

void draw() {
  background(32);
  lights();


  pos.x = sin(radians(frameCount)) * 200;
  pos.y = cos(radians(frameCount)) * 200;  

  pushMatrix();
  translate(width / 2, height / 2, 0); 

  rotateX(rotY);
  rotateY(rotX);



  pushMatrix();

  translate(pos.x, pos.y, pos.z);

  ellipse(0,0,20,20);

  popMatrix();

  for (int j = 0; j < model.getSegmentCount(); j++) {
    Segment segment = model.getSegment(j);
    Face[] faces = segment.getFaces();

    // draw faces
    noStroke();

    beginShape(TRIANGLES);
    for (int i = 0; i < faces.length; i++) {
      Face f = faces[i];
      PVector[] vs = f.getVertices();

      // if the majority of the face is pointing to the position we draw it.
      if(f.isFacingPosition(pos)){

        for (int k = 0; k < vs.length; k++)
          vertex(vs[k].x, vs[k].y, vs[k].z);

      }
    }
    endShape();


    beginShape(LINES);
    // draw face normals
    for (int i = 0; i < faces.length; i++) {
      PVector v = faces[i].getCenter();
      PVector n = faces[i].getNormal();

      // scale the alpha of the stroke by the facing amount.
      // 0.0 = directly facing away
      // 1.0 = directly facing the point 
      stroke(255, 0, 255, 255.0 * faces[i].getFacingAmount(pos));

      vertex(v.x, v.y, v.z);
      vertex(v.x + (n.x * normLength), v.y + (n.y * normLength), v.z + (n.z * normLength));

    }
    endShape();    


  }
  popMatrix();
}

void mouseDragged() {
  rotX += (mouseX - pmouseX) * 0.01;
  rotY -= (mouseY - pmouseY) * 0.01;
}








