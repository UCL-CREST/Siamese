	public static long checksum1(File file) throws IOException {
		CRC32 crc = new CRC32();
		FileReader fr = new FileReader(file);
		int data;
		while((data = fr.read()) != -1) {
			crc.update(data);
		}
		fr.close();
		return crc.getValue();
	}
