package wt;

public enum EFileType {
	JAR(".jar"),

	JAVA(".java"),

	CLASS(".class");
	
	private String type;

	private EFileType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public boolean isMy(String fileName) {
		return fileName.endsWith(type);
	}

}
