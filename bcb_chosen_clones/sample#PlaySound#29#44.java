	public static void playSound2(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
		AudioFormat audioFormat = inputStream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		sourceLine.open(audioFormat);
		sourceLine.start();
		int nbytes = 0;
		byte[] data = new byte[1024];
		while(nbytes != -1) {
			nbytes = inputStream.read(data, 0, data.length);
			sourceLine.write(data, 0, data.length);
		}
		sourceLine.drain();
		sourceLine.close();
	}
