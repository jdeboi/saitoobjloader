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

import processing.core.PConstants;

/**
 * @author tatsuyas
 * @author mattDitton
 * @author Ekene Ijeoma
 * 
 *         Each model element contains the indexes to the vertices, normals and
 *         UV's needed to make a face
 */

public class Element implements PConstants {

	public int indexType = POLYGON;

	public Vector indices;
	public Vector vertexIndices;
	public Vector normalIndices;

	/**
	 * Constructor for the Element. A model element is all the indices
	 * needed to draw a face.
	 */
	public Element() {
		indices = new Vector();
		vertexIndices = new Vector();
		normalIndices = new Vector();
	}

	public int getIndexCount() {
		return indices.size();
	}

	public int[] getVertexIndices() {
		int[] v = new int[getIndexCount()];

		for (int i = 0; i < v.length; i++)
			v[i] = getVertexIndex(i);

		return v;
	}

	public int[] getNormalIndices() {
		int[] v = new int[getIndexCount()];

		for (int i = 0; i < v.length; i++)
			v[i] = getNormalIndex(i);

		return v;
	}

	public int[] getTextureIndices() {
		int[] v = new int[getIndexCount()];

		for (int i = 0; i < v.length; i++)
			v[i] = getTextureIndex(i);

		return v;
	}

	// Arrays start at 0 (hence the -1) But OBJ files start the
	// indices at 1.
	public int getVertexIndex(int i) {
		return ((Integer) indices.elementAt(i)).intValue() - 1;
	}

	public int getTextureIndex(int i) {
		return ((Integer) vertexIndices.elementAt(i)).intValue() - 1;
	}

	public int getNormalIndex(int i) {
		return ((Integer) normalIndices.elementAt(i)).intValue() - 1;
	}
}
