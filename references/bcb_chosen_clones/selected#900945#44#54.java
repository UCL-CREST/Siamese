    public static void copyFile(File fromFile, File toFile) throws OWFileCopyException {
        try {
            FileChannel src = new FileInputStream(fromFile).getChannel();
            FileChannel dest = new FileOutputStream(toFile).getChannel();
            dest.transferFrom(src, 0, src.size());
            src.close();
            dest.close();
        } catch (IOException e) {
            throw (new OWFileCopyException("An error occurred while copying a file", e));
        }
    }
