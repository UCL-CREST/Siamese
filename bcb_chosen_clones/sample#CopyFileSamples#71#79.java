	public static void copyFile6(File srcFile, File destFile) throws FileNotFoundException {
		Scanner s = new Scanner(srcFile);
		PrintWriter pw = new PrintWriter(destFile);
		while(s.hasNextLine()) {
			pw.println(s.nextLine());
		}
		pw.close();
		s.close();
	}
