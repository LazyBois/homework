package wt;

import java.util.Map;
import java.util.Map.Entry;

import core.Group;
import core.struct.GroupResult;
import core.struct.WorkGroup;
import core.struct.WorkObject;

public class GroupJava implements Group {

	@Override
	public GroupResult group(Map<WorkObject, Map<WorkObject, Integer>> sames, int threshold) {
		GroupResult result = new GroupResult();

		for (Entry<WorkObject, Map<WorkObject, Integer>> entry : sames.entrySet()) {

			WorkGroup group = null;
			if (entry.getKey().haveGroup()) {
				group = entry.getKey().getGroup();
			} else {
				group = new WorkGroup(result.applyGroupId());
				group.addMember(entry.getKey());
			}

			for (Entry<WorkObject, Integer> same : entry.getValue().entrySet()) {
				if (!same.getKey().haveGroup() && same.getValue() > threshold) {
					group.addMember(same.getKey());
				}
			}

			result.addGroup(group);
		}

		return result;
	}

}
