import processing.opengl.*;

import saito.objloader.*;

OBJModel model ;
OBJModelBoundingBox bbox;

void setup(){

  size(400,400,OPENGL);

  model = new OBJModel(this, "map_ground_path_s.obj", OBJModel.ABSOLUTE, QUADS);
  model.enableDebug();

  model.scale(100f);
  model.translateToCenter();

  bbox = new OBJModelBoundingBox(this, model);
}


void draw(){
  background(32);
  lights();
  translate(width/2, height/2, 0);
  rotateX(radians(frameCount)/2);
  rotateY(radians(frameCount)/2);
  noStroke();


  for(int i = -1; i < 2; i ++){
    pushMatrix();
    translate(0,0,i*bbox.depth);
    model.draw();
    popMatrix();
  }


  noFill();
  stroke(255,0,255);
  bbox.draw();
  noStroke();
}

