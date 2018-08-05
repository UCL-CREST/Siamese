	public static void deleteRecursively1(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles())
				deleteRecursively1(f);
		}
		file.delete();
	}
