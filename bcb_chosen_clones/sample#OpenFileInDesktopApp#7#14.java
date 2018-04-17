	public static void openFileInDesktopApp(File file) throws IOException {
		Desktop desktop = Desktop.getDesktop();
		if(Desktop.isDesktopSupported()) {
			desktop.open(file);
		} else {
			throw new UnsupportedOperationException("Not supported on this platform.");
		}
	}
