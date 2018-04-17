    public static void copyFile(String sIn, String sOut) throws IOException {
        File fIn = new File(sIn);
        File fOut = new File(sOut);
        FileChannel fcIn = new FileInputStream(fIn).getChannel();
        FileChannel fcOut = new FileOutputStream(fOut).getChannel();
        try {
            fcIn.transferTo(0, fcIn.size(), fcOut);
        } catch (IOException e) {
            throw e;
        } finally {
            if (fcIn != null) fcIn.close();
            if (fcOut != null) fcOut.close();
        }
        fOut.setReadable(fIn.canRead());
        fOut.setWritable(fIn.canWrite());
        fOut.setExecutable(fIn.canExecute());
    }
