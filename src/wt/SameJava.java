package wt;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import core.Same;
import core.struct.WorkObject;

public class SameJava implements Same {

	@Override
	public Map<WorkObject, Map<WorkObject, Integer>> same(List<WorkObject> works) {
		Map<WorkObject, Map<WorkObject, Integer>> result = new HashMap<>();
		for (WorkObject one : works) {
			Map<WorkObject, Integer> sames = new HashMap<>();
			for (WorkObject other : works) {
				if (one.equals(other)) {
					continue;
				}
				sames.put(other, sameJava(one, other));
			}

			result.put(one, sames);
		}
		return null;
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
