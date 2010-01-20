package saito.objloader;

/*
 * Alias .obj loader for processing
 * programmed by Tatsuya SAITO / UCLA Design | Media Arts 
 * Created on 2005/04/17
 *
 * 
 *  
 */

import java.util.ArrayList;
import java.util.Vector;

import processing.core.PConstants;
import processing.core.PVector;

/**
 * @author tatsuyas
 * @author mattDitton
 * @author Ekene Ijeoma
 * 
 *         Each model element contains the indexes to the vertices, normals and
 *         UV's needed to make a face
 */

public class Face implements PConstants {

	public int indexType = POLYGON;

	public ArrayList<Integer> indices;
	public ArrayList<Integer> vertexIndices;
	public ArrayList<Integer> normalIndices;

	public ArrayList<PVector> vertices;
	public ArrayList<PVector> normals;

	/**
	 * Constructor for the Element. A model element is all the indices needed to
	 * draw a face.
	 */
	public Face() {
		indices = new ArrayList<Integer>();
		vertexIndices = new ArrayList<Integer>();
		normalIndices = new ArrayList<Integer>();

		vertices = new ArrayList<PVector>();
		normals = new ArrayList<PVector>();
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

	PVector getNormal() {

		// middle vertex
		PVector m = new PVector();

		for (int i = 0; i < vertices.size(); i++)
			m.add(vertices.get(i));

		m.div(vertices.size());

		// middle - first vertex
		PVector aToB = PVector.sub(m, vertices.get(0));

		// middle - last vertex
		PVector cToB = PVector.sub(m, vertices.get(vertices.size() - 1));

		PVector n = cToB.cross(aToB);

		return n.normalize(new PVector(1, 1, 1));
	}

	// Arrays start at 0 (hence the -1) But OBJ files start the
	// indices at 1.
	public int getVertexIndex(int i) {
		return indices.get(i) - 1;
	}

	public int getTextureIndex(int i) {
		return vertexIndices.get(i) - 1;
	}

	public int getNormalIndex(int i) {
		return normalIndices.get(i) - 1;
	}
}
