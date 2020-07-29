	public static void playSound1(File file) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		Clip clip = AudioSystem.getClip();
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
		clip.open(inputStream);
		clip.start();
	}
