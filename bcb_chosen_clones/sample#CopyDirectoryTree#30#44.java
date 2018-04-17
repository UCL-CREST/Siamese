	public static void copyDirectory2(final Path src, final Path dest) throws IOException {
		Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Files.copy(dir, dest.resolve(src.relativize(dir)));
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.copy(file, dest.resolve(src.relativize(file)));
				return FileVisitResult.CONTINUE;
			}
		});
	}
