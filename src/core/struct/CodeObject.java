package core.struct;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CodeObject {
	
	private WorkObject workObject;

	private StudentObject student;

	private final Path pathRoot;

	private Set<Path> javaFiles;

	private Set<Path> classFiles;

	private Map<Path, List<String>> javaContent;


	public CodeObject(StudentObject student, Path pathRoot) {
		this.student = student;
		this.pathRoot = pathRoot;
		this.javaFiles = new HashSet<>();
		this.classFiles = new HashSet<>();
	}

	public StudentObject getStudent() {
		return student;
	}

	public void setStudent(StudentObject student) {
		this.student = student;
	}

	public Set<Path> getJavaFiles() {
		return javaFiles;
	}

	public Set<Path> getClassFiles() {
		return classFiles;
	}

	public Path getPathRoot() {
		return pathRoot;
	}

	public Map<Path, List<String>> getJavaContent() {
		return javaContent;
	}

	public void setJavaContent(Map<Path, List<String>> javaContent) {
		this.javaContent = javaContent;
	}

	public void setJavaFiles(Set<Path> javaFiles) {
		this.javaFiles = javaFiles;
	}

	public void setClassFiles(Set<Path> classFiles) {
		this.classFiles = classFiles;
	}

	public WorkObject getWorkObject() {
		return workObject;
	}

	public void setWorkObject(WorkObject workObject) {
		this.workObject = workObject;
	}
	
}
