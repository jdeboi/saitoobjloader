package saito.objloader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Vector;

import processing.core.PApplet;
 

public class ModelSegment {
	public Vector elements; 
 
	public String mtlName;
	
	public IntBuffer vindexesIB;
	public IntBuffer tindexesIB;
	public IntBuffer nindexesIB;
	
	int[] vertind = new int[0];
	int[] texind = new int[0];
	int[] normind = new int[0];
	
	
	public ModelSegment() {
		elements = new Vector();
	}

	public String getMtlname() {
		return mtlName;
	}
	
	public Object getElement(int index){
		return elements.elementAt(index);
	}

	public int getSize() {
		return elements.size();
	}
	
	public void setupOPENGL(Debug debug){

		for (int j = 0; j <  getSize(); j ++){
		
			ModelElement tmpf = (ModelElement) (getElement(j));
			
			if(j == 0){
				vertind = tmpf.getVertexIndexArray();
				normind = tmpf.getNormalIndexArray();
				texind =  tmpf.getTextureIndexArray();
			}
			else{
				vertind = PApplet.concat(vertind, tmpf.getVertexIndexArray());
				normind = PApplet.concat(normind, tmpf.getNormalIndexArray());
				texind =  PApplet.concat(texind,  tmpf.getTextureIndexArray());
			}
		}
		
		debug.println("Number of vert indexes = " + vertind.length);
		debug.println("Number of Normal indexes = " + normind.length);
		debug.println("Number of Texture indexes = " + texind.length);
		
		//vindexesIB = setupIntBuffer(vertind);
		
		vindexesIB = ByteBuffer.allocateDirect(4 * vertind.length).order(ByteOrder.nativeOrder()).asIntBuffer();
		vindexesIB.put(vertind);
		vindexesIB.rewind();
		
		nindexesIB = setupIntBuffer(normind);
		tindexesIB = setupIntBuffer(texind);
		
		debug.println(vindexesIB.toString());
		debug.println(nindexesIB.toString());
		debug.println(tindexesIB.toString());
		
		debug.println(" " + vindexesIB.hasArray());
		
	}
	
	private IntBuffer setupIntBuffer(int[] i){
		
		IntBuffer fb = ByteBuffer.allocateDirect(4 * i.length).order(ByteOrder.nativeOrder()).asIntBuffer();
		fb.put(i);
		fb.rewind();
		
		return fb;
		
	}
}
 
