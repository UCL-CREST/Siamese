    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream fIn;
        FileOutputStream fOut;
        FileChannel fIChan, fOChan;
        long fSize;
        MappedByteBuffer mBuf;
        fIn = new FileInputStream(src);
        fOut = new FileOutputStream(dest);
        fIChan = fIn.getChannel();
        fOChan = fOut.getChannel();
        fSize = fIChan.size();
        mBuf = fIChan.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
        fOChan.write(mBuf);
        fIChan.close();
        fIn.close();
        fOChan.close();
        fOut.close();
    }
