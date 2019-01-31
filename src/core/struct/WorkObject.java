package core.struct;

public class WorkObject {

	private StudentObject student;

	private CodeObject code;

	private WorkGroup group;

	// 作业编号
	private int workId;

	public WorkObject(StudentObject student, CodeObject code, int workId) {
		this.student = student;
		this.code = code;
		this.workId = workId;

		this.student.setWorkObject(this);
		this.code.setWorkObject(this);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof WorkObject)) {
			return false;
		}

		WorkObject other = (WorkObject) obj;
		return this.workId == other.getWorkId();
	}

	public StudentObject getStudent() {
		return student;
	}

	public void setStudent(StudentObject student) {
		this.student = student;
	}

	public CodeObject getCode() {
		return code;
	}

	public void setCode(CodeObject code) {
		this.code = code;
	}

	public boolean haveGroup() {
		return group != null;
	}

	public int getWorkId() {
		return workId;
	}

	public void setWorkId(int workId) {
		this.workId = workId;
	}

	public WorkGroup getGroup() {
		return group;
	}

	public void setGroup(WorkGroup group) {
		this.group = group;
	}

}
