package core.input;

import java.util.List;

import core.struct.WorkObject;

public interface InputWork {

	/**
	 * @param read
	 * @return 以学生id->学生作业的格式读入作业
	 */
	public List<WorkObject> read();

	// 获取阈值
	public int threshold();

}
