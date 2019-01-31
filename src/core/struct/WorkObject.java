package core.struct;

public class WorkObject {
	/** 未分组 */
	public static int UN_GROUP = -1;

	private StudentObject student;

	private CodeObject code;

	private int groupId = UN_GROUP;

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

	public static int getUN_GROUP() {
		return UN_GROUP;
	}

	public static void setUN_GROUP(int uN_GROUP) {
		UN_GROUP = uN_GROUP;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public boolean haveGroup() {
		return groupId != UN_GROUP;
	}

}
