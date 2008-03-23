package saito.objloader;

import processing.core.PApplet;

public class Debug {
	
	public boolean enabled = true;
	
	PApplet parent;
	
	public Debug(PApplet parent){
		this.parent = parent;
	}
	public void println(String str){
		if(enabled)
			PApplet.println(str);
	}
	public void print(String str){
		if(enabled)
			PApplet.print(str);
	}
}
