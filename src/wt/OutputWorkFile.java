package wt;

import java.util.Map.Entry;

import core.output.OutputWork;
import core.struct.GroupResult;
import core.struct.WorkGroup;

public class OutputWorkFile implements OutputWork {

	@Override
	public void write(GroupResult group) {
		for (Entry<Integer, WorkGroup> entry : group.getGroups().entrySet()) {
			System.out.println(entry.getKey());
			entry.getValue().getMember().forEach(student -> {
				System.out.println(student.getStudent().toString());
			});
		}
	}

}
