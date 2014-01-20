package springplugin.utils;

public class MethodInfo {

	private String name;
	private int line;
	
	public MethodInfo() {
	}
	public MethodInfo(String name,int line) {
		this.name = name;
		this.line = line;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	
}
