package wt;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import core.HomeWork;
import core.input.InputWork;
import core.output.OutputWork;
import core.struct.GroupResult;
import core.struct.StudentObject;
import core.struct.WorkGroup;
import core.struct.WorkObject;

public class HomeWorkWT implements HomeWork {

	private InputWork reader;

	private OutputWork writer;

	private GroupResult groups;

	private Map<StudentObject, WorkObject> data;

	@Override
	public void doWork() {
		reader = new InputWorkFile("resource", 5000);
		writer = new OutputWorkFile();
		groups = new GroupResult();
		data = reader.read();

		for (WorkObject one : data.values()) {

			Map<StudentObject, Integer> sames = new HashMap<>();
			for (WorkObject other : data.values()) {
				if (one == other) {
					continue;
				}
				sames.put(other.getStudent(), sameJava(one, other));
			}

			groups.getSames().put(one.getStudent(), sames);
		}

		groupJava(reader.threshold());

		writer.write(groups);
	}

	private void groupJava(int threshold) {
		// 二维相似矩阵按照相似度阈值划聚类
		Map<StudentObject, Map<StudentObject, Integer>> sames = groups.getSames();

		for (Entry<StudentObject, Map<StudentObject, Integer>> entry : sames.entrySet()) {
			WorkGroup group = null;
			if (entry.getKey().getWorkObject().haveGroup()) {
				group = groups.getGroups().get(entry.getKey().getWorkObject().getGroupId());
			} else {
				group = new WorkGroup(groups.applyGroupId());
				group.addMember(entry.getKey().getWorkObject());
			}

			for (Entry<StudentObject, Integer> same : entry.getValue().entrySet()) {
				if (!same.getKey().getWorkObject().haveGroup() && same.getValue() > threshold) {
					group.addMember(same.getKey().getWorkObject());
				}
			}
			groups.addGroup(group);
		}

	}

	private int sameJava(WorkObject one, WorkObject other) {
		int result = 0;

		// 每个文件与另一份作业的每个文件的相似值取最大值并相加
		// TODO 此处可添加剪枝函数以减少匹配次数
		for (Entry<Path, List<String>> entryOne : one.getCode().getJavaContent().entrySet()) {
			int sameMax = Integer.MIN_VALUE;
			for (Entry<Path, List<String>> entryOther : other.getCode().getJavaContent().entrySet()) {
				int same = sameJava(entryOne.getValue(), entryOther.getValue());
				if (same > sameMax) {
					sameMax = same;
				}
			}
			result += sameMax;
		}

		return result;
	}

	private int sameJava(List<String> little, List<String> big) {
		if (big.size() < little.size()) {
			List<String> temp = little;
			little = big;
			big = temp;
		}

		int sum = 0;
		float len = 0;

		int i = 0;
		int j = 0;

		for (i = 0; i < little.size(); i++) {
			String littleStr = little.get(i);
			String bigStr = big.get(i);

			if (bigStr.length() < littleStr.length()) {
				String temp = bigStr;
				bigStr = littleStr;
				littleStr = temp;
			}

			len += bigStr.length();
			for (j = 0; j < littleStr.length(); j++) {
				if (littleStr.charAt(j) == bigStr.charAt(j)) {
					sum++;
				} else {
					sum--;
				}
			}

			for (; j < bigStr.length(); j++) {
				sum--;
			}
		}

		for (; i < big.size(); i++) {
			sum -= big.get(i).length();
			len += big.get(i).length();
		}

		// 保留四位小数
		int result = (int) (sum / len * 10000);
		return result;
	}
}
