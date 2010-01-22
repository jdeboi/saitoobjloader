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
import java.util.Comparator;

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

public class Face implements PConstants, Comparable {

	public int indexType = POLYGON;

	public ArrayList<Integer> vertexIndices;
	public ArrayList<Integer> uvIndices;
	public ArrayList<Integer> normalIndices;

	public ArrayList<PVector> vertices;
	public ArrayList<PVector> normals;
	public ArrayList<PVector> uvs;

	/**
	 * Constructor for the Element. A model element is all the indices needed to
	 * draw a face.
	 */
	public Face() {
		vertexIndices = new ArrayList<Integer>();
		uvIndices = new ArrayList<Integer>();
		normalIndices = new ArrayList<Integer>();

		vertices = new ArrayList<PVector>();
		normals = new ArrayList<PVector>();
		uvs = new ArrayList<PVector>();
	}

	public int getIndexCount() {
		return vertexIndices.size();
	}

	public int getVertexCount() {
		return vertices.size();
	}

	public int getNormalCount() {
		return normals.size();
	}

	public int getUVCount() {
		return uvs.size();
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

	public PVector[] getVertices() {
		return vertices.toArray(new PVector[vertices.size()]);
	}

	public PVector[] getNormals() {
		return normals.toArray(new PVector[normals.size()]);
	}

	public PVector[] getUvs() {
		return uvs.toArray(new PVector[uvs.size()]);
	}

	public PVector getCenter() {
		PVector c = new PVector();

		for (int i = 0; i < vertices.size(); i++)
			c.add(vertices.get(i));

		c.div(vertices.size());

		return c;
	}

	public PVector getNormal() {
		// center vertex
		PVector c = getCenter();

		// center - first vertex
		PVector aToB = PVector.sub(c, vertices.get(0));
		// center - last vertex
		PVector cToB = PVector.sub(c, vertices.get(vertices.size() - 1));
		PVector n = cToB.cross(aToB);

		n.normalize();

		return n;
	}

	// Arrays start at 0 (hence the -1) But OBJ files start the
	// indices at 1.
	public int getVertexIndex(int i) {
		return vertexIndices.get(i) - 1;
	}

	public int getTextureIndex(int i) {
		return uvIndices.get(i) - 1;
	}

	public int getNormalIndex(int i) {
		return normalIndices.get(i) - 1;
	}

	public boolean isFacingPosition(PVector position) {
		PVector c = getCenter();

		// this works out the vector from the camera to the face.
		PVector positionToFace = new PVector(position.x - c.x, position.y - c.y, position.z - c.z);

		// We now know the vector from the camera to the face,
		// and the vector that describes which direction the face
		// is pointing, so we just need to do a dot-product and
		// based on that we can tell if it's facing the camera or not
		// float result = PVector.dot(cameraToFace, faceNormal);
		float result = positionToFace.dot(getNormal());

		// if the result is positive, then it is facing the camera.
		return result < 0;
	}

	public float getFacingAmount(PVector position) {
		PVector c = getCenter();

		// this works out the vector from the camera to the face.
		PVector positionToFace = new PVector(position.x - c.x, position.y - c.y, position.z - c.z);

		c.normalize();

		positionToFace.normalize();

		return (1.0f - (positionToFace.dot(getNormal()) + 1.0f) / 2.0f);
	}

	public int compareTo(Object f2) throws ClassCastException {
		if (!(f2 instanceof Face))
			throw new ClassCastException("Face object expected.");

		PVector f1Center = getCenter();
		PVector f2Center = ((Face) f2).getCenter();

		return (int) (f1Center.x - f2Center.x);
	}

	public static Comparator<Face> FaceXComparator = new Comparator<Face>() {
		public int compare(Face f1, Face f2) {
			PVector f1Center = f1.getCenter();
			PVector f2Center = f2.getCenter();
			
			return (int) (f1Center.x - f2Center.x);
		}
	};

	public static Comparator<Face> FaceYComparator = new Comparator<Face>() {
		public int compare(Face f1, Face f2) {
			PVector f1Center = f1.getCenter();
			PVector f2Center = f2.getCenter();
			
			return (int) (f1Center.y - f2Center.y);
		}
	};

	public static Comparator<Face> FaceZComparator = new Comparator<Face>() {
		public int compare(Face f1, Face f2) {
			PVector f1Center = f1.getCenter();
			PVector f2Center = f2.getCenter();
			
			return (int) (f1Center.z - f2Center.z);
		}
	};
}
