package wt;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) throws Exception {
//
//		Path target = Paths.get("resource", "test");
////		Files.createDirectories(target);
//		Path path = Paths.get("resource");
//		try (DirectoryStream<Path> entries = Files.newDirectoryStream(path, "[0-9]*.jar")) {
//			entries.forEach((k) -> {
////				System.out.println(k.getFileName());
//				try {
//					readZIP(k, target);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//		}

//		testSt();
		
		rule();
	}

	public static void readZIP(Path path, Path target) throws IOException {
		FileSystem fSystem = FileSystems.newFileSystem(path, null);

		Files.walkFileTree(fSystem.getPath("/"), new SimpleFileVisitor<Path>() {
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				Path tPath = Paths.get("resource", "test", file.toAbsolutePath().toString());
				if (Files.notExists(tPath.getParent())) {
					Files.createDirectories(tPath.getParent());
				}

				Files.move(file, tPath, StandardCopyOption.REPLACE_EXISTING);

				System.out.println(file.toAbsolutePath().toString());

				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static void testSt() {
		String[] test = {"   "," import ","import "," importsfd","package "," package ","////","*","/*","packagesd"," public"," public class CodeObject {"};
		for(String string : test) {
			strIsCode(string);
		}
	}

	private static boolean strIsCode(String line) {
		// 去掉空格/注释/packgae/import
		if (line.matches("[\\s]*(?![/|*|])(?!import )(?!package )[a-zA-Z]+.*")) {
			System.out.println(line);
			return true;
		}
		return false;
	}
	
	public static void rule() {
		Pattern pattern = Pattern.compile("[0-9]+_[\\u4e00-\\u9fa5]+_[0-9]+");
		Matcher matcher = pattern.matcher("1001_王彤_1.jar");
		if(matcher.find()) {
			System.out.println(matcher.group());
		}
	}

}
