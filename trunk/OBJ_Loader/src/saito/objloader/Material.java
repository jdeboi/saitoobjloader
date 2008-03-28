package saito.objloader;

/*
 * Alias .obj loader for processing
 * programmed by Tatsuya SAITO / UCLA Design | Media Arts 
 * Created on 2005/04/17
 *
 * 
 *  
 */

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
		Ka = new float[3];
		Kd = new float[3];
		Ks = new float[3];
		for (int i = 0; i < Ka.length; i++) {
			Ka[i] = 0.5f;
			Kd[i] = 0.5f;
			Ks[i] = 0.5f;
		}
		d = 1.0f;
		
		mtlName = "default";
	}
}
