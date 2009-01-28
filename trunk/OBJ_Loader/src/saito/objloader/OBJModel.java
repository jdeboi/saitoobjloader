package saito.objloader;
/*
 * Alias .obj loader for processing
 * programmed by Tatsuya SAITO / UCLA Design | Media Arts 
 * Created on 2005/04/17
 *
 * 
 *
 */


import processing.core.*;
import processing.opengl.*;

import java.io.BufferedReader; 
import java.io.InputStream;
import java.io.InputStreamReader; 
import java.util.Hashtable;
import java.util.Vector;
//import java.nio.*;

import javax.media.opengl.*;

/**
 * 
 * @author tatsuyas
 * @author mditton
 * 
 * @TODO: Add documentation and examples to the google code repository
 * @TODO: Add java doc commenting
 * @TODO: Add vertex normals and face normals from Collada Loader
 * @TODO: Add getNormal() function
 * @TODO: Use getNormal to push verts along normals in example
 * 
 * google code address (because I always forget)
 * 
 * http://code.google.com/p/saitoobjloader/
 * 
 */


public class OBJModel implements PConstants{

	// global variables
	Vector vertexes; // vertexes

	Vector texturev; // texture coordinates

	Vector normv;

	Vector modelSegments;

	Hashtable materials;
	
	Hashtable groups;

	String objName = "default";

	String defaultMaterialName = "default";

	Group defaultGroup = new Group("default");

	ModelSegment defaultModelSegment = new ModelSegment();

	// processing variables
	PApplet parent;

	PImage texture; // texture image applied from the code.

	// runtime rendering variables
	int mode = TRIANGLES; // render mode (ex. POLYGON, POINTS ..)

	private boolean flagTexture = true;

	private boolean flagMaterial = true;

	private boolean flagLocalTexture = false;

	public Debug debug;

	String originalTexture;
	
	GL gl;

	// -------------------------------------------------------------------------
	// ------------------------------------------------------------ Constructors
	// -------------------------------------------------------------------------
	/**
	 * Class Constructor to setup an empty obj model
	 */
	public OBJModel(PApplet parent) {
		
		setup(parent);

	}
	
	//extra constructor because I got sick of having two lines to create and load the model. - MD
	/**
	 * Class Constructor, loads the string as an obj from the data directory
	 */
	public OBJModel(PApplet parent, String s) {
		
		setup(parent);
		
		load(s);
		
	}
	
	// -------------------------------------------------------------------------
	// ------------------------------------------------------------------- Setup
	// -------------------------------------------------------------------------
	private void setup(PApplet parent){
		
		this.parent = parent;

		parent.registerDispose(this);

		vertexes = new Vector();

		texturev = new Vector();

		normv = new Vector();

		groups = new Hashtable();

		modelSegments = new Vector();

		materials = new Hashtable();

		debug = new Debug(parent);

		debug.enabled = false;
		
	}
	
	/*
	 * Called after loading the obj model. This will setup the Vertex buffer objects ready for the drawOPENGL method
	 */

	public void setupOPENGL(){
		
		if(!(parent.g instanceof PGraphicsOpenGL))
		{
			
			throw new RuntimeException("This feature requires OpenGL");
			// I mean seriously, the function is called setupOPENGL!!!
		}
		
		gl = ((PGraphicsOpenGL) parent.g).gl;
		
		debug.println("Setting up OPENGL buffers");
		
		debug.println("Generating Buffers");
			    
		debug.println("number of model segments = " + modelSegments.size());
		
		ModelSegment tmpModelSegment;
		
		Material mtl;
		
		for (int i = 0; i < modelSegments.size(); i ++){
			
			tmpModelSegment = (ModelSegment) modelSegments.elementAt(i);
			
			if(tmpModelSegment.getSize() != 0){ // Why are there empty model segments? KAHN!!!  - MD
				
				debug.println("number of model elements = "    + tmpModelSegment.getSize());
				debug.println("model element uses this mtl = " + tmpModelSegment.getMtlname());
				
				tmpModelSegment.setupOPENGL(gl, debug , vertexes, texturev, normv);
				
				mtl = (Material) materials.get(tmpModelSegment.mtlName);
				
				mtl.setupOPENGL(gl, debug);
				
			}
		}
	    
	    debug.println("leaving setup function");
	}
	
//  simple normaize function. Might need this for the OPENGL normals	
//	private void  normalize (Vertex v)
//	{
//	    // calculate the length of the vector
//	    float len = (float)(Math.sqrt((v.vx * v.vx) + (v.vy * v.vy) + (v.vz * v.vz)));
//
//	    // avoid division by 0
//	    if (len == 0.0f)
//	        len = 1.0f;
//
//	    // reduce to unit size
//	    v.vx /= len;
//	    v.vy /= len;
//	    v.vz /= len;
//	}
	
	/*
	 * Draws the obj model using the Vertex Buffers that were made in the setupOPENGL method
	 */
	public void drawOPENGL()
	{

		boolean fill = parent.g.fill;
		boolean stroke = parent.g.stroke;
		
		parent.fill(255);
		parent.stroke(255);
		parent.noFill();
		parent.noStroke();
		
		gl=((PGraphicsOpenGL)parent.g).beginGL();
		
		saveGLState();
		
	    ModelSegment tmpModelSegment;
	    Material mtl;
	    
	    for (int i = 0; i < modelSegments.size(); i ++){
			
			tmpModelSegment = (ModelSegment) modelSegments.elementAt(i);
			
			if(tmpModelSegment.getSize() != 0){ //again with the empty model segments WTF?

				mtl = (Material) materials.get(tmpModelSegment.mtlName);
				
				mtl.useOPENGLStart(gl, flagMaterial, flagTexture);
	
			    switch(mode){
		    
				    case(POINTS):
			    		tmpModelSegment.drawOPENGL(gl, GL.GL_POINTS);
				    break;
				    
				    case(LINES):
				    	tmpModelSegment.drawOPENGL(gl, GL.GL_LINES);
				    break;
				    
				    case(TRIANGLES):
				    	tmpModelSegment.drawOPENGL(gl, GL.GL_TRIANGLES);
				    break;
				    
				    case(TRIANGLE_STRIP):
				    	tmpModelSegment.drawOPENGL(gl, GL.GL_TRIANGLE_STRIP);
				    break;
				    
				    case(QUADS):
				    	tmpModelSegment.drawOPENGL(gl, GL.GL_QUADS);
				    break;
				    
				    case(QUAD_STRIP):
				    	tmpModelSegment.drawOPENGL(gl, GL.GL_QUAD_STRIP);
				    break;
				    
				    case(POLYGON):
				    	tmpModelSegment.drawOPENGL(gl, GL.GL_POLYGON);
				    break;
			    	
			    }

		    	mtl.useOPENGLFinish(gl, flagMaterial, flagTexture);	    

			}
		}    
	    
	    restoreGLState();
	    
		((PGraphicsOpenGL)parent.g).endGL();
		
		parent.g.fill = fill;
		parent.g.stroke = stroke;
	}
	
	private void saveGLState()

    {
		
        gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);

		gl.glMatrixMode(GL.GL_MODELVIEW);

        gl.glPushMatrix();

        gl.glMatrixMode(GL.GL_PROJECTION);

        gl.glPushMatrix();

    }      

   

	private void restoreGLState()

    {

        gl.glMatrixMode(GL.GL_PROJECTION);

        gl.glPopMatrix();

        gl.glMatrixMode(GL.GL_MODELVIEW);

        gl.glPopMatrix();

        gl.glPopAttrib();

    }
	
	
	// -------------------------------------------------------------------------
	// ------------------------------------------------------------------- Utils
	// -------------------------------------------------------------------------
	
	public void showModelInfo() {

		debug.println("Obj Name: \t\t" + objName);
		debug.println("");
		debug.println("V  Size: \t\t" + vertexes.size());
		debug.println("Vt Size: \t\t" + texturev.size());
		debug.println("Vn Size: \t\t" + normv.size());
		debug.println("G  Size: \t\t" + groups.size());
		debug.println("S  Size: \t\t" + modelSegments.size());
		debug.println("");
	}
	
	public void debugMode() {
		debug.enabled = true;
		debug.println("");
		debug.println("objloader version 015");
		debug.println("17 October 2008");
		debug.println("http://code.google.com/p/saitoobjloader/");		
		//debug.println("http://users.design.ucla.edu/~tatsuyas/tools/objloader/index.htm");
		//debug.println("http://www.polymonkey.com/2008/page.asp?obj_loader");
		debug.println("");
		
	}
	
	public void clear() {
		vertexes.clear();
		texturev.clear();
		normv.clear();
		groups.clear();
		modelSegments.clear();
		materials.clear();
		debug.println("OBJModel is empty");
		
	}
	
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------	
	// ------------------------------------------------------------ Load Toggles
	// ---------------------------------------------- called before load(String)
	// ---------------- Can be used in the case of the mtl having absolute paths 
	// ------------------------------------------------------ -----Nice work XSI
	// ------ If this is true the texture will be loaded from the data directory
	
	public void disableLocalTexture() {
		flagLocalTexture = false;
		debug.println("local tex:\t\toff");
	}

	public void enableLocalTexture() {
		flagLocalTexture = true;
		debug.println("local tex:\t\ton");
	}
	
	// -------------------------------------------------------------------------
	// ----------------------------------------------------------- Draw Booleans
	// -------------------------------------------------------------------------

	public void disableTexture() {
		flagTexture = false;
		debug.println("texture:\t\toff");
	}

	public void enableTexture() {
		flagTexture = true;
		debug.println("texture:\t\ton");
	}

	public void disableMaterial() {
		flagMaterial = false;
		debug.println("material:\t\toff");
	}

	public void enableMaterial() {
		flagMaterial = true;
		debug.println("material:\t\ton");
	}

	public void texture(PImage tex) {

		texture = tex;
		debug.println("Using new texture");
	}
	
	public void drawMode(int mode) {
		this.mode = mode;
		
		switch(mode){
			case(POINTS):
				debug.println("draw mode:\t\tPOINTS");
				break;
			
			case(LINES):
				debug.println("draw mode:\t\tLINES");
				break;
				
			case(POLYGON):
				debug.println("draw mode:\t\tPOLYGON");
				break;
				
			case(TRIANGLES):
				debug.println("draw mode:\t\tTRIANGLES");
				break;
				
			case(TRIANGLE_STRIP):
				debug.println("draw mode:\t\tTRIANGLE_STRIP");
				break;
				
			case(QUADS):
				debug.println("draw mode:\t\tQUADS");
				break;
				
			case(QUAD_STRIP):
				debug.println("draw mode:\t\t");
				break;
		}
	}
	
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------- Draw
	// -------------------------------------------------------------------------
	public void draw() {
		
		drawModel();
		
	}

	public void drawModel() {
		
		try {
			Vertex v = null, vt = null, vn = null;

			int vtidx = 0, vnidx = 0, vidx = 0;

			Material mtl = null;

			// render all triangles
			for (int s = 0; s < modelSegments.size(); s++) {

				boolean bTexture = true;

				ModelSegment tmpModelSegment = (ModelSegment) modelSegments.elementAt(s);

				mtl = (Material) materials.get(tmpModelSegment.mtlName);
				
				// if the material is not assigned for some
				// reason, it uses the default material
				// setting
				if (mtl == null) 
				{
					mtl = (Material) materials.get(defaultMaterialName);

					debug.println("Material '" + tmpModelSegment.mtlName + "' not defined");
				}

				if (flagMaterial) {
					
					parent.fill(255.0f * mtl.Ka[0], 255.0f * mtl.Ka[1], 255.0f * mtl.Ka[2], 255.0f * mtl.d);
					
				}

				for (int f = 0; f < tmpModelSegment.getSize(); f++) {

					ModelElement tmpf = (ModelElement) (tmpModelSegment.getElement(f));

					parent.textureMode(PApplet.NORMALIZED);

					parent.beginShape(mode); // specify render mode

					if (tmpf.getSize() == 0){  // look more empty f'ing model segments
						
						continue;
					}

					if (flagTexture == false){
						
						bTexture = false;
					}

					if (mtl.map_Kd == null){

						bTexture = false;
					}

					if (bTexture){

						if (texture != null){

							parent.texture(texture); // setting applied texture
						}

						else{

							parent.texture(mtl.map_Kd); // setting texture from mtl info
						}							
					}
					
					if (tmpf.getSize() > 0) {

						for (int fp = 0; fp < tmpf.getSize(); fp++) {

							vidx = tmpf.getVertexIndex(fp);

							v = (Vertex) vertexes.elementAt(vidx);

							if (v != null) {

								try {

									if (tmpf.nindexes.size() > 0) {

										vnidx = tmpf.getNormalIndex(fp);

										vn = (Vertex) normv.elementAt(vnidx);

										parent.normal(vn.vx, vn.vy, vn.vz);
									}

									if (bTexture) {

										vtidx = tmpf.getTextureIndex(fp);
										

										vt = (Vertex) texturev.elementAt(vtidx);

										parent.vertex(v.vx, -v.vy, v.vz, vt.vx, 1.0f - vt.vy);

									} 
									else{

										parent.vertex(v.vx, -v.vy, v.vz);
									}

								} 
								catch (Exception e) {

									e.printStackTrace();
								}
							}

							else {
								
								parent.vertex(v.vx, -v.vy, v.vz);
							}

						}
					}
					parent.endShape();
					
					parent.textureMode(PApplet.IMAGE);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	// -------------------------------------------------------------------------
	// ------------------------------------------------------------- Obj Loading 
	// -------------------------------------------------------------------------

	public void load(String filename) {
		
		parseOBJ(getBufferedReader(filename));

		if (debug.enabled) {
			this.showModelInfo();
		}

	}
	
	//TODO: Pretty sure I could kill this off and use PApplet.loadStrings - MD
	public BufferedReader getBufferedReader(String filename) {

		BufferedReader retval = null;

		try {

			// URL url = null;
			InputStream is = null;

			/*
			 * parent.openStream(arg0); if (filename.startsWith("http://")) {
			 * try { url = new URL(filename); retval = new BufferedReader(new
			 * InputStreamReader(parent.openStream(filename))); return retval; }
			 * catch (MalformedURLException e) { e.printStackTrace(); return
			 * null; } catch (IOException ioe) { ioe.printStackTrace(); return
			 * null; } }
			 */

			is = parent.openStream(filename);
			if (is != null) {
				try {
					retval = new BufferedReader(new InputStreamReader(is));
					return retval;
				}

				catch (Exception ioe) {
					ioe.printStackTrace();
					return null;
				}
			}

			/*
			 * is = getClass().getResourceAsStream("/data/" + filename); if (is !=
			 * null) { try { retval = new BufferedReader(new
			 * InputStreamReader(is)); return retval; } catch (Exception ioe) {
			 * ioe.printStackTrace(); return null; } }
			 * 
			 * url = getClass().getResource("/" + filename); if (url != null) {
			 * System.out.println(url.toString()); try { url = new
			 * URL(filename); retval = new BufferedReader(new
			 * InputStreamReader(parent.openStream())); return retval; } catch
			 * (MalformedURLException e) { e.printStackTrace(); return null; }
			 * catch (IOException ioe) { ioe.printStackTrace(); return null; } }
			 * 
			 * url = getClass().getResource("/data/" + filename); if (url !=
			 * null) { System.out.println(url.toString()); try { url = new
			 * URL(filename); retval = new BufferedReader(new
			 * InputStreamReader(url .openStream())); return retval; } catch
			 * (MalformedURLException e) { e.printStackTrace(); return null; }
			 * catch (IOException ioe) { ioe.printStackTrace(); return null; } }
			 * 
			 * try { // look inside the sketch folder (if set) String location =
			 * parent.sketchPath + File.separator + "data"; File file = new
			 * File(location, filename); if (file.exists()) { retval = new
			 * BufferedReader(new FileReader(file)); return retval; } } catch
			 * (IOException e) { e.printStackTrace(); return null; } // ignored
			 * 
			 * try { File file = new File("data", filename); if (file.exists()) {
			 * retval = new BufferedReader(new FileReader(file)); return retval; } }
			 * catch (IOException ioe) { ioe.printStackTrace(); }
			 * 
			 * try { File file = new File(filename); if (file.exists()) { retval =
			 * new BufferedReader(new FileReader(file)); return retval; } }
			 * catch (IOException ioe) { ioe.printStackTrace(); return null; }
			 */
		} catch (SecurityException se) {
		} // online, whups

		parent.die("Could not find .OBJ file " + filename, null);

		return retval;
	}
	
	// -------------------------------------------------------------------------
	// ------------------------------------------------------------- Obj Parsing 
	// -------------------------------------------------------------------------
	
	// read this as b-read not bread like the bakery - MD
	private void parseOBJ(BufferedReader bread) {
		try {

			// adding default variables to the global data table
			// creating the default group

			groups.put("default", defaultGroup);

			Group currentGroup = defaultGroup;

			// creating the default material

			Material defaultMaterial = new Material();

			defaultMaterial.mtlName = defaultMaterialName;

			materials.put(defaultMaterialName, defaultMaterial);

			String currentMaterial = defaultMaterialName;

			// creating the default model segment

			modelSegments.add(defaultModelSegment);

			defaultModelSegment.mtlName = currentMaterial;

			currentGroup.segments.add(defaultModelSegment);

			ModelSegment currentModelSegment = defaultModelSegment;

			String line;

			while ((line = bread.readLine()) != null) {
				// debug.println(line);
				// parse the line

				String[] elements = line.split("\\s+");

				// if not a blank line, process the line.
				if (elements.length > 0) {

					// analyze the format
					if (elements[0].equals("v")) { // point vector
						Vertex tmpv = new Vertex();
						tmpv.vx = Float.valueOf(elements[1]).floatValue();
						tmpv.vy = Float.valueOf(elements[2]).floatValue();
						tmpv.vz = Float.valueOf(elements[3]).floatValue();
						vertexes.add(tmpv);
					} 
					else if (elements[0].equals("vn")) { // normal vector
						Vertex tmpv = new Vertex();
						tmpv.vx = Float.valueOf(elements[1]).floatValue();
						tmpv.vy = Float.valueOf(elements[2]).floatValue();
						tmpv.vz = Float.valueOf(elements[3]).floatValue();
						normv.add(tmpv);
					} 
					else if (elements[0].equals("vt")) {
						Vertex tmpv = new Vertex();
						tmpv.vx = Float.valueOf(elements[1]).floatValue();
						tmpv.vy = Float.valueOf(elements[2]).floatValue();
						texturev.add(tmpv);
					} 
					else if (elements[0].equals("o")) {
						if (elements[1] != null)
							objName = elements[1];
					} 
					else if (elements[0].equals("mtllib")) {
						if (elements[1] != null)
							this.parseMTL(this.getBufferedReader(elements[1]));
					}

					// elements that needs to consider the current context

					else if (elements[0].equals("g")) { // grouping
						// setting
						ModelSegment newModelSegment = new ModelSegment();

						modelSegments.add(newModelSegment);
						
						currentModelSegment = newModelSegment;

						currentModelSegment.mtlName = currentMaterial;

						for (int e = 1; e < elements.length; e++) {
							
							if (groups.get(elements[e]) == null) {
								
								// debug.println("group '" + elements[e] +"'");
								Group newGroup = new Group(elements[e]);
								
								groups.put(elements[e], newGroup);
								
							}
						}
					} 
					
					else if (elements[0].equals("usemtl")) { 

						// material setting
						
						ModelSegment newModelSegment = new ModelSegment();
						
						modelSegments.add(newModelSegment);
						
						currentModelSegment = newModelSegment;
						
						currentModelSegment.mtlName = elements[1];

					} 
					
					else if (elements[0].equals("f")) { // Element
						
						ModelElement tmpf = new ModelElement();

						if (elements.length < 3) {
							
							debug.println("Warning: potential model data error");
							
						}

						for (int i = 1; i < elements.length; i++) {
							
							String seg = elements[i];
							
							if (seg.indexOf("/") > 0) {
								
								String[] forder = seg.split("/");

								if (forder.length > 2) {
									
									if (forder[0].length() > 0){
										
										tmpf.indexes.add(Integer.valueOf(forder[0]));
									}
									
									if (forder[1].length() > 0){
										
										tmpf.tindexes.add(Integer.valueOf(forder[1]));
									}
									
									if (forder[2].length() > 0){
										
										tmpf.nindexes.add(Integer.valueOf(forder[2]));
									}
									
								} 
								else if (forder.length > 1) {
									
									if (forder[0].length() > 0){
										
										tmpf.indexes.add(Integer.valueOf(forder[0]));
									}
									
									if (forder[1].length() > 0){
										
										tmpf.tindexes.add(Integer.valueOf(forder[1]));
									}
									
								} 
								else if (forder.length > 0) {
									
									if (forder[0].length() > 0){
										
										tmpf.indexes.add(Integer.valueOf(forder[0]));
									}
									
								}
							} 
							else {
								
								if (seg.length() > 0){
									
									tmpf.indexes.add(Integer.valueOf(seg));
								}
							}
							
						}
						
						currentModelSegment.elements.add(tmpf);
						
					} 
					else if (elements[0].equals("ll")) { // line
						
						ModelElement tmpf = new ModelElement();
						
						tmpf.iType = ModelElement.POLYGON;

						if (elements.length < 2) {
							
							debug.println("Warning: potential model data error");
							
						}

						for (int i = 1; i < elements.length; i++) {
							
							tmpf.indexes.add(Integer.valueOf(elements[i]));
							
						}

						currentModelSegment.elements.add(tmpf);

					}
				}
			}
			
		} 
		catch (Exception e) {
			
			e.printStackTrace();
			
		}
		// until I figure out where the empty model Segments are coming from this will have to do.
		for(int i = 0; i < getSegmentSize(); i ++){
			
			if(getIndexCountInSegment(i) == 0 ){ //again with the empty model segments WTF?
				
				modelSegments.remove(i);
			}
		}
	}

	// -------------------------------------------------------------------------
	// ------------------------------------------------------------- MTL Parsing 
	// -------------------------------------------------------------------------
	
	private void parseMTL(BufferedReader bread) {
		try {
			
			String line;
			
			Material currentMtl = null;

			while ((line = bread.readLine()) != null) {
				
				// parse the line
				
				String elements[] = line.split("\\s+");
				
				if (elements.length > 0) {
					// analyze the format

					if (elements[0].equals("newmtl")) {
						
						debug.println("material: \t\t'" + elements[1] + "'");
						
						String mtlName = elements[1];
						
						Material tmpMtl = new Material();
						
						currentMtl = tmpMtl;
						
						materials.put(mtlName, tmpMtl);
						
					}
					else if (elements[0].equals("map_Ka") && elements.length > 1) {

						debug.println("texture ambient \t\t'" + elements[1] + "'");

						// String texname = elements[1];
						// currentMtl.map_Ka = parent.loadImage(texname);
						

					}
					else if (elements[0].equals("map_Kd") && elements.length > 1) {

						if (!flagLocalTexture) {
							debug.println("texture diffuse \t\t'" + elements[1] + "'");
						}

						String texname = elements[1];

						if (flagLocalTexture) {

							int p1 = 0;
							
							// TODO get the system folder slash. (where is that pocket java guide when you need it) 
							String slash = "\\";
							
							while (p1 != -1) {
								
								p1 = texname.indexOf(slash);
								texname = texname.substring(p1 + 1);
							}
							
							debug.println("diffuse: \t\t'" + texname + "'");

						}

						currentMtl.map_Kd = parent.loadImage(texname);
						
						originalTexture = texname;

					} 
					else if (elements[0].equals("Ka") && elements.length > 1) {
						
						currentMtl.Ka[0] = Float.valueOf(elements[1]).floatValue();
						currentMtl.Ka[1] = Float.valueOf(elements[2]).floatValue();
						currentMtl.Ka[2] = Float.valueOf(elements[3]).floatValue();
						
					}
					else if (elements[0].equals("d") && elements.length > 1) {
						
						currentMtl.d = Float.valueOf(elements[1]).floatValue();
						
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------------------------------------
	// --------------------------------------------------- Get and Set Functions 
	// -------------------------------------------------------------------------

	public int getGroupsize() {
		return this.groups.size();
	}

	public Group getGroup(String groupName) {
		return (Group) this.groups.get(groupName);
	}
	
	public int getSegmentSize() {
		return this.modelSegments.size();
	}
	
	public int getIndexCountInSegment(int i){
		return  ((ModelSegment)modelSegments.elementAt(i)).getSize();
	}
	
	// there are just to many casts here. It feels very muddy. 
	public int[] getVertIndexArrayInSegment(int i, int num){
		return ((ModelElement)((ModelSegment)modelSegments.elementAt(i)).getElement(num)).getVertexIndexArray();
	}
	
	public int[] getNormalIndexArrayInSegment(int i, int num){
		return ((ModelElement)((ModelSegment)modelSegments.elementAt(i)).getElement(num)).getNormalIndexArray();
	}
	
	public int[] getTextureIndexArrayInSegment(int i, int num){
		return ((ModelElement)((ModelSegment)modelSegments.elementAt(i)).getElement(num)).getTextureIndexArray();
	}

	public int getVertexsize() {
		return this.vertexes.size();
	}

	public Vertex getVertex(int i) {
		return (Vertex) vertexes.elementAt(i);
	}

	public void setVertex(int i, Vertex vertex) {
		Vertex tmpv = (Vertex) vertexes.elementAt(i);
		tmpv.vx = vertex.vx;
		tmpv.vy = vertex.vy;
		tmpv.vz = vertex.vz;
	}
	
	public void setVertex(int i, float x, float y, float z ) {
		Vertex tmpv = (Vertex) vertexes.elementAt(i);
		tmpv.vx = x;
		tmpv.vy = y;
		tmpv.vz = z;
	}

	public void setTexture(PImage textureName) {
		texture = textureName;
	}

	public void originalTexture() {
		texture = parent.loadImage(originalTexture);
	}
	
	// -------------------------------------------------------------------------
	// -------------------------------------------- Processing Library functions
	// -------------- I'm not using them, but I didn't want to forget about them
	
	public void pre() {
	}
	
	public void post() {
	}
	
	public void size(int w, int h) {
	}
	
//	if the mouse and keyboard functions ever get used, these imports will be needed at the top of the package
//	import java.awt.event.KeyEvent;
//	import java.awt.event.MouseEvent;
//	public void mouse(MouseEvent event) {
//	}
//
//	public void key(KeyEvent e) {
//	}

	public void dispose() {
		// System.gc();
	}
	
}
