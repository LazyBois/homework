package core;

import java.util.Map;

import core.struct.GroupResult;
import core.struct.WorkObject;

/**
 * @author wangtong 聚类算法
 */
public interface Group {

	public GroupResult group(Map<WorkObject, Map<WorkObject, Integer>> works, int threshold);

}
