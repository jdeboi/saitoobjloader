import processing.core.*; 
import processing.xml.*; 

import saito.objtools.*; 
import saito.objloader.*; 
import processing.opengl.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class OBJLoader_Sample_SAITO extends PApplet {






OBJModel model ;
OBJModelBoundingBox obox;

float rotX, rotY;

public void setup()
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



public void draw()
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

public void keyPressed()
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

public void mouseDragged()
{
  rotX += (mouseX - pmouseX) * 0.01f;
  rotY -= (mouseY - pmouseY) * 0.01f;
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "OBJLoader_Sample_SAITO" });
  }
}
