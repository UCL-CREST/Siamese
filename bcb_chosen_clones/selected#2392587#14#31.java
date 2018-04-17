    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        Closer c = new Closer();
        try {
            source = c.register(new FileInputStream(sourceFile).getChannel());
            destination = c.register(new FileOutputStream(destFile).getChannel());
            destination.transferFrom(source, 0, source.size());
        } catch (IOException e) {
            c.doNotThrow();
            throw e;
        } finally {
            c.closeAll();
        }
    }
