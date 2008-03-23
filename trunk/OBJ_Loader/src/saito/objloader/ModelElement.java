package saito.objloader;

/*
 * Alias .obj loader for processing
 * programmed by Tatsuya SAITO / UCLA Design | Media Arts 
 * Created on 2005/04/17
 *
 * 
 *  
 */

import java.util.Vector;

/**
 * @author tatsuyas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ModelElement {

	public static int LINE=0;
	public static int POLYGON=1;

	public int iType=POLYGON;
	
	public Vector indexes;
	public Vector tindexes;
	public Vector nindexes;

	public ModelElement() {
		indexes = new Vector();
		tindexes = new Vector();
		nindexes = new Vector();
	}

	public int getSize(){
		return indexes.size();
	}
	public int getVertexIndex(int i){
		return ((Integer)indexes.elementAt(i)).intValue();
	}
	public int getTextureIndex(int i){
		return ((Integer)tindexes.elementAt(i)).intValue();		
	}
	public int getNormalIndex(int i){
		return ((Integer)nindexes.elementAt(i)).intValue();
	}
}
