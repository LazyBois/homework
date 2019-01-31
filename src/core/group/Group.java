package core.group;

import java.util.Map;

import core.struct.GroupResult;
import core.struct.WorkObject;

public interface Group {

	public GroupResult group(Map<String, WorkObject> data);

}
