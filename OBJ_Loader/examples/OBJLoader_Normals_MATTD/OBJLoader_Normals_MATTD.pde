import saito.objloader.*;

import processing.opengl.*;


// declare that we need a OBJModel and we'll be calling it "model"
OBJModel model;
float rotX;
float rotY;

int faceCount;
int face;
float normLength = 0;
float normDir = 1;


void setup()
{
  size(600, 600, OPENGL);

  // making an object called "model" that is a new instance of OBJModel
  model = new OBJModel(this);

  // turning on the debug output (it's all the stuff that spews out in the black box down the bottom)
  model.enableDebug();

  // enableLocalTexture is usefull if you're modeling package uses absolute paths when pointing to the diffuse testure (like XSI)
  // see mtl file in data folder for example
  model.setTexturePathMode(OBJModel.ABSOLUTE);

  //setting the draw mode
  model.shapeMode(TRIANGLES);

  model.disableMaterial();

  model.load("map_ground_path_s.obj");

  faceCount = model.getIndexCountInSegment(0);

  model.scale(150);  


  for(int i = 0; i < model.getSegmentCount(); i++){

    println(model.getIndexCountInSegment(i));
  }

  println(faceCount);

  println(model.getSegmentCount());

  noStroke();
}

void draw()
{
  background(32);
  lights();

  pushMatrix();
  translate(width/2, height/2, 0);
  rotateX(rotY);
  rotateY(rotX);

  normLength += normDir;
  
  if(normLength < -30 || normLength > 30){
   
   normDir = - normDir; 
    
  }
  

  for(int j = 0; j < model.getSegmentCount(); j++)
  {

    faceCount = model.getIndexCountInSegment(j);

    if(frameCount % 2 == 0)
    {
      face = (face + 1) % faceCount;
    }

    // the original face

    int[] vertIndex = model.getVertexIndicesInSegment(j, face);
    int[] normIndex = model.getNormalIndicesInSegment(j, face);  

    fill(196);
    noStroke();

    beginShape(QUADS);

    for(int i = 0; i < vertIndex.length; i ++)
    {

      PVector v = model.getVertex(vertIndex[i]);

      vertex(v.x, v.y, v.z);

    }

    endShape();

    stroke(255,0,255);

    // the normal

    beginShape(LINES);

    for(int i = 0; i < vertIndex.length; i ++)
    {
      PVector v = model.getVertex(vertIndex[i]);
      PVector n = model.getNormal(normIndex[i]);

      vertex(v.x, v.y, v.z);

      vertex(v.x + (n.x*normLength), v.y + (n.y*normLength), v.z + (n.z*normLength));
    }

    endShape();    


    // the face projected along the normal    
    fill(196,0,196,195);
    noStroke();    

    beginShape(QUADS);

    for(int i = 0; i < vertIndex.length; i ++)
    {

      PVector v = model.getVertex(vertIndex[i]);
      PVector n = model.getNormal(normIndex[i]);

      vertex(v.x + (n.x*normLength), v.y + (n.y*normLength), v.z + (n.z*normLength));

    }
    endShape();

  }

  popMatrix();
}


void mouseDragged()
{
  rotX += (mouseX - pmouseX) * 0.01;
  rotY -= (mouseY - pmouseY) * 0.01;
}




