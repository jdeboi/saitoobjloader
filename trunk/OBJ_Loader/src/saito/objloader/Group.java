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
public class Group {
	public String groupName;
	public Vector segments;

	public Group(String groupName){
		segments = new Vector();
		this.groupName = groupName;
	}
	
	public String getName(){
		return groupName;
	}

}
