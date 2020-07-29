    public File extractID3v2TagDataIntoFile(File outputFile) throws TagNotFoundException, IOException {
        int startByte = (int) ((MP3AudioHeader) audioHeader).getMp3StartByte();
        if (startByte >= 0) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(startByte);
            fc.read(bb);
            FileOutputStream out = new FileOutputStream(outputFile);
            out.write(bb.array());
            out.close();
            fc.close();
            fis.close();
            return outputFile;
        }
        throw new TagNotFoundException("There is no ID3v2Tag data in this file");
    }
