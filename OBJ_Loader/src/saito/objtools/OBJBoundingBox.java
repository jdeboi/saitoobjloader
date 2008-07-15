package saito.objtools;

import processing.core.*;
import saito.objloader.*;

public class OBJBoundingBox  implements PConstants{
	
	PApplet parent;
	private float x1 = MAX_FLOAT,y1 = MAX_FLOAT,z1 = MAX_FLOAT,x2 = MIN_FLOAT,y2 = MIN_FLOAT,z2 = MIN_FLOAT;
	private float centerX, centerY,centerZ;
	public float width, height, depth;
	
	int fillColor;
	int strokeColor;
	
	boolean fill;
	boolean stroke;
	
	public OBJBoundingBox(PApplet parent, OBJModel model){
		
		this.parent = parent;
		
		PApplet.println("OBJBoundingBox - \tGetting the Bounding Box");
		
		int numberOfVerts = model.getVertexsize();
		
		if(numberOfVerts == 0){
			
			PApplet.println("OBJBoundingBox - \tThe model has no verts. Have you loaded it yet?");
			
		}
		else{
		
			Vertex v;
			
			for(int i = 0; i < numberOfVerts; i++){
				
				v = model.getVertex(i);
				
				x1 = PApplet.min(x1,v.vx);
				y1 = PApplet.min(y1,v.vy);
				z1 = PApplet.min(z1,v.vz);
				
				x2 = PApplet.max(x2,v.vx);
				y2 = PApplet.max(y2,v.vy);
				z2 = PApplet.max(z2,v.vz);
				
			}
			
			width =  Math.abs(x1) + Math.abs(x2);
			height = Math.abs(y1) + Math.abs(y2);
			depth =  Math.abs(z1) + Math.abs(z2);
			
			centerX = x1 + (( x2 - x1 )/ 2);
			centerY = y1 + (( y2 - y1 )/ 2);
			centerZ = z1 + (( z2 - z1 )/ 2);
		}
	}
	
	
	
	public void draw(){
		
//		fillColor = parent.g.fillColor;
//		strokeColor = parent.g.strokeColor;
//		
//		fill = parent.g.fill;
//		stroke = parent.g.stroke;
//		
		parent.rectMode(CORNERS);
		
//		parent.noFill();
//		parent.stroke(255,0,255);
		
		parent.pushMatrix();
		parent.translate(centerX, centerY, centerZ);
	  
		parent.box(width, height, depth);
		parent.popMatrix();
//		
//		parent.fill(fillColor);
//		parent.stroke(strokeColor);
//		
//		parent.g.fill = fill;
//		parent.g.stroke = stroke;
		
	}
	
	public float getMinX(){
		return x1;
	}
	public float getMinY(){
		return y1;
	}
	public float getMinZ(){
		return z1;
	}
	public float getMaxX(){
		return x2;
	}
	public float getMaxY(){
		return y2;
	}
	public float getMaxZ(){
		return z2;
	}
	public float[] getMinXYZ(){
		return new float[]{x1,y1,z1};
	}
	public float[] getMaxXYZ(){
		return new float[]{x2,y2,z2};
	}
	public float getCenterX(){
		return centerX;
	}
	public float getCenterY(){
		return centerY;
	}
	public float getCenterZ(){
		return centerZ;
	}
	public float[] getCenterXYZ(){
		return new float[]{centerX,centerY,centerZ};
	}

}
