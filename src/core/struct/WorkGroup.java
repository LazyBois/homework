package core.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LazyMan 聚类
 */
public class WorkGroup {

	private int groupId;

	private List<WorkObject> member;

	public WorkGroup(int groupId) {
		this.setGroupId(groupId);
		this.member = new ArrayList<>();
	}

	public void addMember(WorkObject workObject) {
		workObject.setGroupId(getGroupId());
		member.add(workObject);
	}

	public List<WorkObject> getMember() {
		return member;
	}

	public void setMember(List<WorkObject> member) {
		this.member = member;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
