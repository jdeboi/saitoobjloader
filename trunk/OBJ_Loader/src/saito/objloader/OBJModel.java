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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader; // import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader; // import java.net.MalformedURLException;
// import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @author tatsuyas
 * 
 * TODO: 
 * Add documentation and examples to the google code repository
 * Add vertex normals and face normals from Collada Loader
 * Add getNormal() function
 * Use getNormal to push vers along normals in example
 * Add drawOPENGL() draw mode. Move model data into array lists see this for example http://processing.org/discourse/yabb_beta/YaBB.cgi?board=OpenGL;action=display;num=1206221585;start=1#1
 * 
 */

public class OBJModel {

	// global variables
	Vector vertexes; // vertexes

	Vector texturev; // texture coordinates

	Vector normv;

	Hashtable materials;

	Hashtable groups;

	Vector modelSegments;

	String objName = "default";

	String defaultMaterialName = "default";

	Group defaultGroup = new Group("default");

	ModelSegment defaultModelSegment = new ModelSegment();

	// processing variables
	PApplet parent;

	PImage texture; // texture image applied from the code.

	// runtime rendering variables
	int mode = PApplet.POLYGON; // render mode (ex. POLYGON, POINTS ..)

	boolean flagTexture = true;

	boolean flagMaterial = true;

	boolean flagLocalTexture = false;

	Debug debug;

	String originalTexture;

	public OBJModel(PApplet parent) {
		this.parent = parent;

		parent.registerDispose(this);

		vertexes = new Vector();

		texturev = new Vector();

		normv = new Vector();

		groups = new Hashtable();

		modelSegments = new Vector();

		materials = new Hashtable();

		debug = new Debug(parent);

		// debug.enabled = true; // use this for pre release builds

		// debug.println("objloader 013 pre release check website soon for the
		// final release");

		// debug.println("http://users.design.ucla.edu/~tatsuyas/tools/objloader/index.htm"
		// + "\n");

		debug.enabled = false;
	}

	public void pre() {
		// do something cool
	}

	public void draw() {
		drawModel();
	}

	public void size(int w, int h) {
	}

	public void post() {
	}

	public void mouse(MouseEvent event) {
	}

	public void key(KeyEvent e) {
	}

	public void dispose() {
		// System.gc();
	}

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

	public void disableLocalTexture() {
		flagLocalTexture = false;
		debug.println("local tex:\t\toff");
	}

	public void enableLocalTexture() {
		flagLocalTexture = true;
		debug.println("local tex:\t\ton");
	}

	public void clear() {
		vertexes.clear();
		texturev.clear();
		normv.clear();
		groups.clear();
		modelSegments.clear();
		materials.clear();
		debug.println("OBJModel is empty");
		debug.println("");

	}

	public void texture(PImage tex) {
		/*
		 * try { PImage image = (PImage) tex.clone(); } catch (Exception e) { }
		 * if (image.width > image.height){ }
		 */
		texture = tex;

	}

	public void drawModel() {
		try {
			Vertex v = null, vt = null, vn = null;

			int vtidx = 0, vnidx = 0, vidx = 0;

			Material mtl = null;

			// render all triangles
			for (int s = 0; s < modelSegments.size(); s++) {

				boolean bTexture = true;

				ModelSegment tmpModelSegment = (ModelSegment) modelSegments
						.elementAt(s);

				mtl = (Material) materials.get(tmpModelSegment.mtlName);

				if (mtl == null) // if the material is not assigned for some
									// reason, it uses the default material
									// setting
				{
					mtl = (Material) materials.get(defaultMaterialName);

					debug.println("Material '" + tmpModelSegment.mtlName
							+ "' not defined");
				}

				if (flagMaterial) {
					parent.fill(255.0f * mtl.Ka[0], 255.0f * mtl.Ka[1],
							255.0f * mtl.Ka[2], 255.0f * mtl.d);
				}

				for (int f = 0; f < tmpModelSegment.elements.size(); f++) {

					ModelElement tmpf = (ModelElement) (tmpModelSegment.elements
							.elementAt(f));

					parent.textureMode(PApplet.NORMALIZED);

					parent.beginShape(mode); // specify render mode

					if (flagTexture == false)

						bTexture = false;

					if (tmpf.tindexes.size() == 0)

						bTexture = false;

					if (mtl.map_Kd == null)

						bTexture = false;

					if (bTexture)

						if (texture != null)

							parent.texture(texture); // setting applied
														// texture

						else

							parent.texture(mtl.map_Kd); // setting texture from
														// mtl info

					if (tmpf.indexes.size() > 0) {

						for (int fp = 0; fp < tmpf.indexes.size(); fp++) {

							vidx = ((Integer) (tmpf.indexes.elementAt(fp)))
									.intValue();

							v = (Vertex) vertexes.elementAt(vidx - 1);

							if (v != null) {

								try {

									if (tmpf.nindexes.size() > 0) {

										vnidx = ((Integer) (tmpf.nindexes
												.elementAt(fp))).intValue();

										vn = (Vertex) normv
												.elementAt(vnidx - 1);

										parent.normal(vn.vx, vn.vy, vn.vz);
									}

									if (bTexture) {

										vtidx = ((Integer) (tmpf.tindexes
												.elementAt(fp))).intValue();

										vt = (Vertex) texturev
												.elementAt(vtidx - 1);

										parent.vertex(v.vx, -v.vy, v.vz, vt.vx,
												1.0f - vt.vy);

									} else

										parent.vertex(v.vx, -v.vy, v.vz);

								} catch (Exception e) {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawMode(int mode) {
		this.mode = mode;

		if (mode == 16) {
			debug.println("draw mode:\t\tPOINTS");
		}

		if (mode == 32) {
			debug.println("draw mode:\t\tLINES");
		}

		if (mode == 256) {
			debug.println("draw mode:\t\tPOLYGON");
		}

		if (mode == 64) {
			debug.println("draw mode:\t\tTRIANGLES");
		}

		if (mode == 65) {
			debug.println("draw mode:\t\tTRIANGLE_STRIP");
		}

		if (mode == 128) {
			debug.println("draw mode:\t\tQUADS");
		}

		if (mode == 129) {
			debug.println("draw mode:\t\tQUAD_STRIP");
		}
	}

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

	public void load(String filename) {
		parseOBJ(getBufferedReader(filename));

		if (debug.enabled) {
			this.showModelInfo();
		}

	}

	public void parseOBJ(BufferedReader bread) {
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
					} else if (elements[0].equals("vn")) { // normal vector
						Vertex tmpv = new Vertex();
						tmpv.vx = Float.valueOf(elements[1]).floatValue();
						tmpv.vy = Float.valueOf(elements[2]).floatValue();
						tmpv.vz = Float.valueOf(elements[3]).floatValue();
						normv.add(tmpv);
					} else if (elements[0].equals("vt")) {
						Vertex tmpv = new Vertex();
						tmpv.vx = Float.valueOf(elements[1]).floatValue();
						tmpv.vy = Float.valueOf(elements[2]).floatValue();
						texturev.add(tmpv);
					} else if (elements[0].equals("o")) {
						if (elements[1] != null)
							objName = elements[1];
					} else if (elements[0].equals("mtllib")) {
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
					} else if (elements[0].equals("usemtl")) { // material
						// setting
						ModelSegment newModelSegment = new ModelSegment();
						modelSegments.add(newModelSegment);
						currentModelSegment = newModelSegment;
						currentModelSegment.mtlName = elements[1];

					} else if (elements[0].equals("f")) { // Element
						ModelElement tmpf = new ModelElement();

						if (elements.length < 3) {
							debug
									.println("Warning: potential model data error");
						}

						for (int i = 1; i < elements.length; i++) {
							String seg = elements[i];
							if (seg.indexOf("/") > 0) {
								String[] forder = seg.split("/");

								if (forder.length > 2) {
									if (forder[2].length() > 0)
										tmpf.nindexes.add(Integer
												.valueOf(forder[2]));
									if (forder[1].length() > 0)
										tmpf.tindexes.add(Integer
												.valueOf(forder[1]));
									if (forder[0].length() > 0)
										tmpf.indexes.add(Integer
												.valueOf(forder[0]));
								} else if (forder.length > 1) {
									if (forder[1].length() > 0)
										tmpf.tindexes.add(Integer
												.valueOf(forder[1]));
									if (forder[0].length() > 0)
										tmpf.indexes.add(Integer
												.valueOf(forder[0]));
								} else if (forder.length > 0) {
									if (forder[0].length() > 0)
										tmpf.indexes.add(Integer
												.valueOf(forder[0]));
								}
							} else {
								if (seg.length() > 0)
									tmpf.indexes.add(Integer.valueOf(seg));
							}
						}
						currentModelSegment.elements.add(tmpf);
					} else if (elements[0].equals("ll")) { // line
						ModelElement tmpf = new ModelElement();
						tmpf.iType = ModelElement.POLYGON;

						if (elements.length < 2) {
							debug
									.println("Warning: potential model data error");
						}

						for (int i = 1; i < elements.length; i++) {
							tmpf.indexes.add(Integer.valueOf(elements[i]));
						}

						currentModelSegment.elements.add(tmpf);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseMTL(BufferedReader bread) {
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
					} else if (elements[0].equals("map_Ka")
							&& elements.length > 1) {

						debug.println("texture ambient \t\t'" + elements[1]
								+ "'");
						// String texname = elements[1];
						// currentMtl.map_Ka = parent.loadImage(texname);

					} else if (elements[0].equals("map_Kd")
							&& elements.length > 1) {

						if (!flagLocalTexture) {
							debug.println("texture diffuse \t\t'" + elements[1]
									+ "'");
						}

						String texname = elements[1];

						if (flagLocalTexture) {

							int p1 = 0;
							String slash = "\\";
							while (p1 != -1) {
								p1 = texname.indexOf(slash);
								texname = texname.substring(p1 + 1);
							}
							debug.println("diffuse: \t\t'" + texname + "'");

						}

						currentMtl.map_Kd = parent.loadImage(texname);
						originalTexture = texname;

					} else if (elements[0].equals("Ka") && elements.length > 1) {
						currentMtl.Ka[0] = Float.valueOf(elements[1])
								.floatValue();
						currentMtl.Ka[1] = Float.valueOf(elements[2])
								.floatValue();
						currentMtl.Ka[2] = Float.valueOf(elements[3])
								.floatValue();
					} else if (elements[0].equals("d") && elements.length > 1) {
						currentMtl.d = Float.valueOf(elements[1]).floatValue();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Functions for addressing each group/Element/vertex */

	public int getGroupsize() {
		return this.groups.size();
	}

	public Group getGroup(String groupName) {
		return (Group) this.groups.get(groupName);
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

	public void debugMode() {
		debug.enabled = true;
		debug.println("");
		debug.println("objloader version 013");
		debug.println("http://users.design.ucla.edu/~tatsuyas/tools/objloader/index.htm");
		debug.println("http://www.polymonkey.com/2008/page.asp?obj_loader");
		debug.println("");

	}

	public void setTexture(PImage textureName) {
		texture = textureName;
	}

	public void originalTexture() {
		texture = parent.loadImage(originalTexture);
	}
}
