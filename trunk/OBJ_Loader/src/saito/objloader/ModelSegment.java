package saito.objloader;

import java.util.Vector;
 

public class ModelSegment {
	public Vector elements; 
 
	public String mtlName;

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
}
 
