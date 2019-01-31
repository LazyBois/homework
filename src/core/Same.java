package core;

import java.util.List;
import java.util.Map;

import core.struct.WorkObject;

/**
 * @author wangtong 相似度算法
 */
public interface Same {

	/**
	 * 相似度二维矩阵
	 */
	public Map<WorkObject, Map<WorkObject, Integer>> same(List<WorkObject> works);

}
