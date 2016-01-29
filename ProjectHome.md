# .OBJ loader for Processing #

Maintained by SAITO and Matt Ditton (AKA Polymonkey)

SAITO:
http://users.design.ucla.edu/~tatsuyas/tools/objloader/index.htm

Matt Ditton:
http://thequietvoid.com/

## Updates ##
  * 12/12/2010 : rev23 fix to models without materials. Also cleaner errors for missing mtl files.
  * 28/10/2010 : rev22 fix to loading obj files from zbrush. Also fixes models with missing normals.
  * 01/08/2010 : rev20 massive updates all round.
  * 23/07/2009 : rev18 update to OBJTools to include void hackUVClampToZeroOne(OBJModel m) and void hackUVMapToZeroOne(OBJModel m) functions. These are "in case of emergency break UV's" functions. Added by request.
  * 18/07/2009 : rev17 fixed materials not showing up when mtl file has extra tabs at the front of lines. Bug found by Noirmain Pierre.
  * 16/06/2009 : rev16 added int getTotalFaceCount() to get the number of total faces. And PVector[.md](.md) getFaceVertArray(int faceNumber) this returns an array of PVectors that make up the positions of the verts of the face. These two methods are mainly used for extracting position information from the model.
  * 16/04/2009 : rev15 moved Vert, Normal, and UV's to be stored in the processing PVector object. Added getNormal methods and objTools. And bug fix for filling with diffuse color
  * 12/07/2008 : rev14 added the first pass of the fast draw method
  * 07/01/2007 : rev13 added some material features (by polymonkey)
  * 05/29/2007 : rev12 fixed a texture coordinate bug. texture swapping (thanks to polymonkey)
  * 03/10/2007 : rev11 fixed some major bugs.
  * 01/30/2006 : rev10 fixed some bugs.
  * 11/16/2005 : rev09 supported Processing 95+.
  * 06/18/2005 : rev07 added functions for model transformation
  * 04/05/2005 : rev06 fixed a few bugs, supports alpha value
  * 28/04/2005 : rev05 applet execution support
  * 26/04/2005 : rev04 fixed a texture mapping problem
  * 25/04/2005 : added FAQ
  * 24/04/2005 : rev03 fixed a few bugs, 3ds exported .obj support
  * 19/04/2005 : rev02 .mtl support, normal vertex support, lighting support
  * 16/04/2005 : rev01 launched

## Description ##

This is Wavefront Alias .OBJ file loader for Processing. It loads .OBJ model file and renders the model onto a screen.

## Download ##

http://code.google.com/p/saitoobjloader/downloads/list


 Installation

After uncompressing the file, copy 'objloader/' folder into 'libraries/' folder which you can find under the Processing folder. Since the archive file is compressed in MacOSX environment, it might have some needless hidden files. Notice that the file you need is only 'objloader' folder and the files in the folder.

The folder/file structure should be as follows.

![http://users.design.ucla.edu/~tatsuyas/tools/objloader/fig.jpg](http://users.design.ucla.edu/~tatsuyas/tools/objloader/fig.jpg)

Restart Processing. You should be able to import the objloader library from sketch menu.

## Reference ##

This library contains a class for loading/rendering a .OBJ file (OBJModel) and a class for accessing each vertex (Vertex) which can be primarily used to addressing vertexes in the model file and transform it.

For a complete list of documentation go and check the site <a href='http://thequietvoid.com/client/objloader/reference/index.html'>here</a>.


## FAQ ##

  * What is an .OBJ file?

> .OBJ file is a standard 3D object file format by Alias.

> Alias is the leading software company of 3D graphics technology for the film, video, game development, interactive media, industrial design, automotive industry and visualization softwares. Their .OBJ ASCII file format is widely accepted all over the world as a standard format for exchanging data between 3D graphics applications. OBJ files contain solids which are made up of 3 or 4 sided faces and material data such as texture is searatedly stored in .MTL file.


  * What is .MTL file?

> .OBJ file often has a link to .MTL file, which contains material information such as texture, color and surface reflection. To use the material information, you need to add .MTL file to the project. Currently, OBJModel class supports only texture information. Texture file images (.jpg) should be added to your project too.

> Future updates will include support for other material information.

  * Polygons are not correctly rendered. What should I do?

> The first thing you should make sure is what type of polygons your model is composed of. If it is made of quads, the correct option for drawMode() is POLYGON. If it is made of triangles, drawMode(TRIANGLES) still works. drawMode(TRIANGLES) is recommended for Processing BETA 85 because of some rendering bugs (shapes drawn as POLYGONS sometimes don't show up.) More detailed info is available here (discource: Libraries, Tools - New Libraries: OBJ Loader, Google Web API )

> If the model still doesn't show up, please send me the Processing source code and .OBJ model files you are trying to render. mattditton at gmail.com


  * Texture is not correctly shown. What should I do?

> To render texture, three files have to be added to your project: .OBJ file, .MTL file, texture image file.

> OBJModel uses standard Processing functions to render models. Inside the library, texture image is stored as PImage object, which means that the texture image file format has to be supported by PImage. Currently it supports .jpg.


  * I still have a problem with online execution

> Hmm.. I updated the library so that it can load models files by relative path whereever a sketch program is located. It might still have a bug. Please send me the model file and the sketch program you have a problem with.

## Examples ##


All examples can be viewed <a href='http://thequietvoid.com/client/objloader/#examples'>here</a>


## Etc. ##

Bugs, opinions and complains? Please drop a line here: http://code.google.com/p/saitoobjloader/w/list

