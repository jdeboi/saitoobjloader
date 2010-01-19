import saito.objtools.*;
import saito.objloader.*;

import processing.opengl.*;

OBJModel model ;
OBJModelBoundingBox obox;

float rotX, rotY;

void setup()
{
  size(800, 600, OPENGL);
  frameRate(30);
  model = new OBJModel(this);
  model.enableDebug();
  model.shapeMode(TRIANGLES);
  model.load("dma.obj");
 
  model.scale(20);
  model.translateToCenter();
  
  obox = new OBJModelBoundingBox(this,model);
  
  println(obox.getCenterXYZ());
  
  model.setupGL();
 // stroke(255);
  noStroke();

}



void draw()
{
  background(129);

  //fill(255);
  lights();
  pushMatrix();
  translate(width/2, height/2, 0);
  rotateX(rotY);
  rotateY(rotX);
  model.draw();
  noFill();
  stroke(255);
  obox.draw();
  noStroke();
  translate(0,obox.height,0);
  model.drawGL();
  popMatrix();
}

boolean bTexture = true;
boolean bStroke = false;

void keyPressed()
{
  if(key == 't'){
    if(!bTexture){
      model.enableTexture();
      bTexture = true;
    } 
    else {
      model.disableTexture();
      bTexture = false;
    }
  }

  if(key == 's'){
    if(!bStroke){
      stroke(10, 10, 10, 100);
      bStroke = true;
    } 
    else {
      noStroke();
      bStroke = false;
    }
  }

  else if(key=='1')
    model.shapeMode(POINTS);
  else if(key=='2')
    model.shapeMode(LINES);
  else if(key=='3')
    model.shapeMode(TRIANGLES);
}

void mouseDragged()
{
  rotX += (mouseX - pmouseX) * 0.01;
  rotY -= (mouseY - pmouseY) * 0.01;
}
