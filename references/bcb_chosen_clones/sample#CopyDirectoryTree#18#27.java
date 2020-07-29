	public static void copyDirectory1(Path src, Path dest) throws IOException {
		Files.copy(src, dest);
		if(Files.isDirectory(src)) {
			for(String filename : src.toFile().list()) {
				Path srcFile = src.resolve(filename);
				Path destFile = dest.resolve(filename);
				copyDirectory1(srcFile, destFile);
			}
		}
	}
