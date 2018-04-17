	public static void loadFont1(URL url) throws FontFormatException, IOException {
		InputStream stream = url.openStream();
		Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
	}
