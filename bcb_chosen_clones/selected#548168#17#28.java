    public static final boolean deleteAll(File what) throws FileNotFoundException, IOException {
        if (what == null) {
            return false;
        }
        if (what.isDirectory()) {
            File[] files = what.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
            }
        }
        return what.delete();
    }
