    public static boolean deleteDirectory(final File pPath) {
        if (pPath.exists()) {
            final File[] files = pPath.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return pPath.delete();
    }
