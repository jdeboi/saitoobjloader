import processing.core.*; 
import processing.xml.*; 

import saito.objloader.*; 
import saito.objtools.*; 

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

public class ObJLoader_UV_MATTD extends PApplet {




// declare that we need a OBJModel and we'll be calling it "model"
OBJModel model;
float rotX;
float rotY;

int direction = 0;


public void setup()
{
  size(600, 600, P3D);

  // making an object called "model" that is a new instance of OBJModel
  model = new OBJModel(this);

  // turning on the debug output (it's all the stuff that spews out in the black box down the bottom)
  model.enableDebug();

  // enableLocalTexture is usefull if you're modeling package uses absolute paths when pointing to the diffuse testure (like XSI)
  // see mtl file in data folder for example
  model.setTexturePathMode(OBJModel.ABSOLUTE);  

  //setting the draw mode
  model.shapeMode(TRIANGLES);

//  model.disableMaterial();

  model.load("cube_sphere_test.obj");



//  println("the model has this many segments = " + model.getSegmentCount());
//
//  for (int i = 0; i < model.getSegmentCount(); i ++){
//
//    println("segment " + i + " has this many indexes = " + model.getIndexCountInSegment(i));
//
//    for (int j = 0; j < model.getIndexCountInSegment(i); j ++){
//
//      println(model.getVertIndexArrayInSegment(i,j));
//
//    }
//  }
//
//  faceCount = model.getIndexCountInSegment(1);

  noStroke();
}

int count = 0;

public void draw()
{
  background(128);
  lights();

  pushMatrix();
  translate(width/2, height/2, 0);
  rotateX(rotY);
  rotateY(rotX);
  scale(3);

  model.draw();
  popMatrix();
  
  for(int i = 0; i < model.getUVCount(); i ++)
  {

    PVector u = model.getUV(i);

    u.x = (u.x + (0.01f * direction));
    
  }
  

  
  if(count == 0){
   direction =1; 
  }
  
  if(count%30 == 0)
  {
   direction = - direction; 
  }
    count ++;

}


public void mouseDragged()
{
  rotX += (mouseX - pmouseX) * 0.01f;
  rotY -= (mouseY - pmouseY) * 0.01f;
}



  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "ObJLoader_UV_MATTD" });
  }
}
