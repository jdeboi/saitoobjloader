package saito.objloader;

/*
 * Alias .obj loader for processing
 * programmed by Tatsuya SAITO / UCLA Design | Media Arts 
 * Created on 2005/04/17
 *
 * 
 *  
 */

import javax.media.opengl.GL;

import processing.core.PImage;

/**
 * @author tatsuyas
 * 
 */
public class Material {
	public PImage map_Ka;
	public PImage map_Kd;
	public float[] Ka;
	public float[] Kd;
	public float[] Ks;
	public float d;

	public String mtlName;
	
	public Material() {
		Ka = new float[4];
		Kd = new float[4];
		Ks = new float[4];
		for (int i = 0; i < Ka.length; i++) {
			if(i == 3){
				Ka[i] = 1f;
				Kd[i] = 1f;
				Ks[i] = 1f;
			}
			else{
				Ka[i] = 0.5f;
				Kd[i] = 0.5f;
				Ks[i] = 0.5f;				
			}
		}
		d = 1.0f;
		
		mtlName = "default";
	}
	
	public void useMtlOPENGL(GL gl, Debug debug){
		
		//debug.println("Ambient");
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT,Ka,0);
        
	    if(Kd[3]==1){
	    	//debug.println("Diffuse");
	    	gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT_AND_DIFFUSE,Kd,0);
	    	
	    }
	    
	    else{
	    	//debug.println("Diffuse & Spec");
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT_AND_DIFFUSE,Kd,0);
		    gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR,Ks,0);
		    
	    }
	}
}
