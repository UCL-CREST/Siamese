    public static void buildPerMovieDiffBinary(String completePath, String slopeOneDataFolderName, String slopeOneDataFileName) {
        try {
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + slopeOneDataFolderName + fSep + slopeOneDataFileName);
            FileChannel inC = new FileInputStream(inFile).getChannel();
            for (int i = 1; i <= 17770; i++) {
                File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + slopeOneDataFolderName + fSep + "Movie-" + i + "-SlopeOneData.txt");
                FileChannel outC = new FileOutputStream(outFile).getChannel();
                ByteBuffer buf = ByteBuffer.allocate(17770 * 10);
                for (int j = 1; j < i; j++) {
                    ByteBuffer bbuf = ByteBuffer.allocate(12);
                    inC.position((17769 * 17770 * 6) - ((17769 - (j - 1)) * (17770 - (j - 1)) * 6) + (i - j - 1) * 12);
                    inC.read(bbuf);
                    bbuf.flip();
                    buf.putShort(bbuf.getShort());
                    bbuf.getShort();
                    buf.putInt(bbuf.getInt());
                    buf.putFloat(-bbuf.getFloat());
                }
                buf.putShort(new Integer(i).shortValue());
                buf.putInt(0);
                buf.putFloat(0.0f);
                ByteBuffer remainingBuf = inC.map(FileChannel.MapMode.READ_ONLY, (17769 * 17770 * 6) - ((17769 - (i - 1)) * (17770 - (i - 1)) * 6), (17770 - i) * 12);
                while (remainingBuf.hasRemaining()) {
                    remainingBuf.getShort();
                    buf.putShort(remainingBuf.getShort());
                    buf.putInt(remainingBuf.getInt());
                    buf.putFloat(remainingBuf.getFloat());
                }
                buf.flip();
                outC.write(buf);
                buf.clear();
                outC.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
