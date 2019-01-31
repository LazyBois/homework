package core.struct;

import util.Utils;

public class StudentObject {

	private final String id;

	private final String name;

	private final String classID;

	private WorkObject workObject;

	public StudentObject(String id, String name, String classID) {
		this.id = id;
		this.name = name;
		this.classID = classID;
	}

	public String getClassID() {
		return classID;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public WorkObject getWorkObject() {
		return workObject;
	}

	public void setWorkObject(WorkObject workObject) {
		this.workObject = workObject;
	}

	@Override
	public String toString() {
		return Utils.createStr("{}_{}_{}", id, name, classID);
	}
}
