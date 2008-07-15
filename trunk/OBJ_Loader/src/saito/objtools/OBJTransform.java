package saito.objtools;

import processing.core.*;
import saito.objloader.*;

public class OBJTransform {
	
	PApplet parent;

	
	public OBJTransform(PApplet parent){
		this.parent = parent;
	}
	
	public void scaleOBJ(OBJModel model, float scale){
		
		scaleOBJ(model, scale, scale, scale);
		
	}
	
	public void scaleOBJ(OBJModel model, float scaleX, float scaleY, float scaleZ){
		
		int numberOfVerts = model.getVertexsize();
		
		if(numberOfVerts == 0){
			
			PApplet.println("OBJTransform - \tThe model has no verts. Have you loaded it yet?");
			
		}
		else{
		
			Vertex v;
			
			for(int i = 0; i < numberOfVerts; i++){
				
				v = model.getVertex(i);
				
				v.vx *= scaleX;
				v.vy *= scaleY;
				v.vz *= scaleZ;
				
			}
		}
	}
	
	public void moveOBJ(OBJModel model, float moveX, float moveY, float moveZ){
		
		int numberOfVerts = model.getVertexsize();
		
		if(numberOfVerts == 0){
			
			PApplet.println("OBJTransform - \tThe model has no verts. Have you loaded it yet?");
			
		}
		else{
		
			Vertex v;
			
			for(int i = 0; i < numberOfVerts; i++){
				
				v = model.getVertex(i);
				
				v.vx += moveX;
				v.vy += moveY;
				v.vz += moveZ;
				
			}
		}
	}
	
	public void centerOBJ(OBJModel model){
		
		OBJBoundingBox obox = new OBJBoundingBox(parent, model);
		 
		moveOBJ(model, -obox.getCenterX(), -obox.getCenterY(), -obox.getCenterZ());
		
	}
}