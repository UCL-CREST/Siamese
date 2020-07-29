	public static void loadFont3(File file) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, file);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
	}
