    public static void copyFile(File source, File destination) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel inCh = fis.getChannel();
        FileChannel outCh = fos.getChannel();
        inCh.transferTo(0, inCh.size(), outCh);
        inCh.close();
        fis.close();
        outCh.close();
        fos.flush();
        fos.close();
    }
