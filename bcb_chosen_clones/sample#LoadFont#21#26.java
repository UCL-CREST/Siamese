	public static void loadFont2(File file) throws FontFormatException, IOException {
		FileInputStream stream = new FileInputStream(file);
		Font font = Font.createFont(Font.TYPE1_FONT, stream);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
	}
