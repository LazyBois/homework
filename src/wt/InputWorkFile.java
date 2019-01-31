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
	// 作业路径
	private String workDirPath;

	// 阈值
	private int threshold;

	private String pathRoot = "resource/test";

	public InputWorkFile(String workDirPath, int threshold) {
		this.workDirPath = workDirPath;
		this.threshold = threshold;
	}

	/**
	 * 检查输入目录，如果不符合要求则返回false
	 */
	public boolean check() {
		File file = new File(getWorkDirPath());

		if (!file.exists() || !file.isDirectory()) {
			Log.error("文件不存在或者不是一个目录,path={}", getWorkDirPath());
			return false;
		}

		// 沒文件
		if (file.list().length <= 0) {
			Log.error("空目录,path={}", getWorkDirPath());
			return false;
		}

		return true;
	}

	public Map<StudentObject, WorkObject> read() {
		Map<StudentObject, WorkObject> data = new HashMap<>();
		if (!check()) {
			return data;
		}

		File dir = new File(getWorkDirPath());
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().endsWith(".jar")) {
					return true;
				}
				return false;
			}
		});

		for (File file : files) {
			try {
				WorkObject workObject = readOneWork(file);
				data.put(workObject.getStudent(), workObject);
			} catch (IOException e) {
				Log.error("read filed,path={}", file.getAbsolutePath());
				e.printStackTrace();
			}
		}

		return data;
	}

	// 读入一份作业
	private WorkObject readOneWork(File file) throws IOException {
		WorkObject result = new WorkObject();

		result.setStudent(readStudent(file.getName()));
		result.setCode(readCode(file, result.getStudent()));

		result.getStudent().setWorkObject(result);

		return result;
	}

	private StudentObject readStudent(String fileName) {
		StudentObject result = null;
		String[] words = fileName.split("_");

		if (words.length < 3) {
			return result;
		}

		result = new StudentObject(words[0], words[1], words[2].replaceAll(".jar", ""));
		return result;
	}

	private CodeObject readCode(File file, StudentObject student) throws IOException {
		// unzip
		Path source = Paths.get(file.getAbsolutePath());
		Path targetDir = Paths.get(pathRoot, file.getName().replace(".jar", ""));

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

	public int threshold() {
		return threshold;
	}

	public String getWorkDirPath() {
		return workDirPath;
	}

	public void setWorkDirPath(String workDirPath) {
		this.workDirPath = workDirPath;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
