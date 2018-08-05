    public static void copyFile(File src, File dst) throws ResourceNotFoundException, ParseErrorException, Exception {
        if (src.getAbsolutePath().endsWith(".vm")) {
            copyVMFile(src, dst.getAbsolutePath().substring(0, dst.getAbsolutePath().lastIndexOf(".vm")));
        } else {
            FileInputStream fIn;
            FileOutputStream fOut;
            FileChannel fIChan, fOChan;
            long fSize;
            MappedByteBuffer mBuf;
            fIn = new FileInputStream(src);
            fOut = new FileOutputStream(dst);
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
    }
