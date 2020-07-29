	public static void copyFile4(File srcFile, File destFile) throws IOException {
		InputStream in = new FileInputStream(srcFile);
		OutputStream out = new FileOutputStream(destFile);
		IOUtils.copy(in, out);
		in.close();
		out.close();
	}
