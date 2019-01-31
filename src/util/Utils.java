package util;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author LazyMan 工具集合
 */
public class Utils {

	private static final String STR_REGEX = "\\{\\}";

	public static String createStr(String str, Object... params) {
		if (params == null || params.length == 0) {
			return str;
		}

		for (Object param : params) {
			String paramStr = param == null ? "null" : param.toString();
			str = str.replaceFirst(STR_REGEX, paramStr);
		}

		return str;
	}

	public static void unJAR(Path source, Path targetDir) throws IOException {
		FileSystem fileSystem = FileSystems.newFileSystem(source, null);

		Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<Path>() {

			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path targetFile = Paths.get(targetDir.toString(), file.toAbsolutePath().toString());

				Files.createDirectories(targetFile.getParent());
				Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);

				return FileVisitResult.CONTINUE;
			}

		});

	}
	
	public static boolean strIsCode(String line) {
		// 去掉空格/注释/packgae/import
		if (line.matches("[\\s]*(?![/|*])(?!import )(?!package )[a-zA-Z]+.*")) {
			return true;
		}
		return false;
	}

}
