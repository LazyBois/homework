package wt;

import java.util.List;

import core.HomeWork;
import core.input.InputWork;
import core.output.OutputWork;
import core.struct.GroupResult;
import core.struct.WorkObject;

public class HomeWorkWT implements HomeWork {

	private InputWork reader;

	private OutputWork writer;

	@Override
	public void doWork() {
		// TODO 此处数据应从UI输入
		String inDir = "/Users/wangtong/workspace/ework/workdata";
		String outDir = "/Users/wangtong/workspace/ework/workdata/out";
		String tempDir = "/Users/wangtong/workspace/ework/workdata/temp";
		int thresholdJava = 5000;
		int thresholdClass = 100;
		reader = new InputWorkFile(inDir, outDir, tempDir, thresholdJava, thresholdClass);
		writer = new OutputWorkFile();
		List<WorkObject> data = reader.read();

		SameJava same = new SameJava();
		GroupJava groupJava = new GroupJava();
		GroupResult groups = groupJava.group(same.same(data), reader.threshold());

		writer.write(groups);
	}
}
