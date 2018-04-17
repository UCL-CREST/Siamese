    public static void deleteDirs(String aPathName) {
        try {
            File path = new File(aPathName);
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    deleteDirs(files[i]);
                }
                files[i].delete();
            }
            path.delete();
        } catch (Exception ignored) {
            ignored.printStackTrace(System.err);
        }
    }
