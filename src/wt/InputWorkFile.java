package wt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import core.input.InputWork;
import core.struct.CodeObject;
import core.struct.StudentObject;
import core.struct.WorkObject;
import util.Log;
import util.Utils;

public class InputWorkFile implements InputWork {
	// 输入路径
	private String inDir;
	// 输出路径
	private String outDir;
	// 临时目录
	private String tempDir;

	// java文件阈值
	private int thresholdJava;

	// class文件阈值
	private int thresholdClass;

	public InputWorkFile(String inDir, String outDir, String tempDir, int thresholdJava, int thresholdClass) {
		this.inDir = inDir;
		this.outDir = outDir;
		this.tempDir = tempDir;
		this.thresholdJava = thresholdJava;
		this.thresholdClass = thresholdClass;
	}

	/**
	 * 检查输入目录，如果不符合要求则返回false
	 */
	public boolean check() {
		File file = new File(getInDir());

		if (!file.exists() || !file.isDirectory()) {
			Log.error("文件不存在或者不是一个目录,path={}", getInDir());
			return false;
		}

		// 沒文件
		if (file.list().length <= 0) {
			Log.error("空目录,path={}", getInDir());
			return false;
		}

		return true;
	}

	public List<WorkObject> read() {
		List<WorkObject> data = new ArrayList<>();
		if (!check()) {
			return data;
		}

		File dir = new File(getInDir());
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().matches(REGEX_FILENAME)) {
					Log.info("load file={}", pathname.getName());
					return true;
				}
				return false;
			}
		});

		Log.info("start load file info");
		for (File file : files) {
			try {
				WorkObject workObject = readOneWork(file);
				data.add(workObject);
			} catch (IOException e) {
				Log.error("read filed,path={}", file.getAbsolutePath());
				e.printStackTrace();
			}
		}

		return data;
	}

	/** 作业编号_学号_姓名_班级.jar */
	private static String REGEX_FILENAME = "[0-9]+_[0-9]+_[\\\\u4e00-\\\\u9fa5]+_[0-9]+\\.jar";

	// 读入一份作业
	private WorkObject readOneWork(File file) throws IOException {
		String[] worlds = notSuffix(file.getName()).split("_");
		StudentObject student = new StudentObject(worlds[1], worlds[2], worlds[3]);
		CodeObject code = readCode(file, student);
		WorkObject result = new WorkObject(student, code, Integer.valueOf(worlds[0]));

		Log.info("read java file={}", file.getName());
		return result;
	}

	private CodeObject readCode(File file, StudentObject student) throws IOException {
		// unzip
		Path source = Paths.get(file.getAbsolutePath());
		Path targetDir = Paths.get(tempDir, notSuffix(file.getName()));

		deleteNotEmptyDir(targetDir);
		Files.createDirectories(targetDir);
		Utils.unJAR(source, targetDir);

		// read file
		CodeObject result = new CodeObject(student, targetDir);
		result.setJavaFiles(readJavaFilePath(result.getPathRoot()));
		result.setClassFiles(readClassFilePath(result.getPathRoot()));
		result.setJavaContent(readJavaFile(result));

		return result;
	}

	private void deleteNotEmptyDir(Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}

		Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}

	private Map<Path, List<String>> readJavaFile(CodeObject codeObject) throws IOException {
		Map<Path, List<String>> result = new HashMap<>();

		for (Path path : codeObject.getJavaFiles()) {
			List<String> content = new ArrayList<String>();
			try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(path))) {
				reader.lines().forEach((line) -> {
					if (Utils.strIsCode(line)) {
						StringBuilder one = new StringBuilder();
						for (char ch : line.toCharArray()) {
							if (Character.isLetter(ch) || Character.isDigit(ch)) {
								one.append(Character.toLowerCase(ch));
							}
						}
						content.add(one.toString());
					}
				});

			}
			result.put(path, content);
		}

		return result;
	}

	// 读取此path下的所有java文件路径
	private Set<Path> readJavaFilePath(Path path) throws IOException {
		Set<Path> result = new HashSet<>();

		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (EFileType.JAVA.isMy(file.getFileName().toString())) {
					result.add(file);
				}

				return FileVisitResult.CONTINUE;
			}

		});

		return result;
	}

	private Set<Path> readClassFilePath(Path path) throws IOException {
		Set<Path> result = Files.list(path).filter(new Predicate<Path>() {
			@Override
			public boolean test(Path t) {
				if (t.toAbsolutePath().toString().endsWith(".class")) {
					return true;
				}
				return false;
			}
		}).collect(Collectors.toSet());

		return result;
	}

	private String notSuffix(String fileName) {
		return fileName.replaceAll(EFileType.JAR.getType(), "");
	}

	public String getInDir() {
		return inDir;
	}

	public void setInDir(String inDir) {
		this.inDir = inDir;
	}

	public String getOutDir() {
		return outDir;
	}

	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public int getThresholdJava() {
		return thresholdJava;
	}

	public void setThresholdJava(int thresholdJava) {
		this.thresholdJava = thresholdJava;
	}

	public int getThresholdClass() {
		return thresholdClass;
	}

	public void setThresholdClass(int thresholdClass) {
		this.thresholdClass = thresholdClass;
	}

	@Override
	public int threshold() {
		// TODO Auto-generated method stub
		return 0;
	}
}
