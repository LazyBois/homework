package core.struct;

import java.util.HashMap;
import java.util.Map;

public class GroupResult {

	/** 存储每个学生与其他学生的相似度 **/
	private Map<StudentObject, Map<StudentObject, Integer>> sames = new HashMap<>();

	/**
	 * groupID -> workgroup
	 */
	private Map<Integer, WorkGroup> groups;

	private int groupId = 0;

	public GroupResult() {
		this.setSames(new HashMap<>());
		this.groups = new HashMap<>();
	}

	public int applyGroupId() {
		return groupId++;
	}

	public Map<Integer, WorkGroup> getGroups() {
		return groups;
	}

	public void addGroup(WorkGroup group) {
		groups.put(group.getGroupId(), group);
	}

	public Map<StudentObject, Map<StudentObject, Integer>> getSames() {
		return sames;
	}

	public void setSames(Map<StudentObject, Map<StudentObject, Integer>> sames) {
		this.sames = sames;
	}
}
