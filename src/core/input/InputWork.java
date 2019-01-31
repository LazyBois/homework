package core.input;

import java.util.Map;

import core.struct.StudentObject;
import core.struct.WorkObject;

public interface InputWork {

	/**
	 * @param read
	 * @return 以学生id->学生作业的格式读入作业
	 */
	public Map<StudentObject, WorkObject> read();

	// 获取阈值
	public int threshold();

}
