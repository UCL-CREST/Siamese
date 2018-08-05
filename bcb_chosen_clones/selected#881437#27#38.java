    public static void copyFile(File fileIn, File fileOut) throws IOException {
        FileChannel chIn = new FileInputStream(fileIn).getChannel();
        FileChannel chOut = new FileOutputStream(fileOut).getChannel();
        try {
            chIn.transferTo(0, chIn.size(), chOut);
        } catch (IOException e) {
            throw e;
        } finally {
            if (chIn != null) chIn.close();
            if (chOut != null) chOut.close();
        }
    }
