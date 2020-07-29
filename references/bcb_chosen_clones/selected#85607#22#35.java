    public static boolean deleteRecursively(File folder) {
        if (folder != null && folder.exists()) {
            if (folder.isDirectory()) {
                File[] children = folder.listFiles();
                if (children != null) {
                    for (int i = 0; i < children.length; i++) {
                        deleteRecursively(children[i]);
                    }
                }
            }
            return folder.delete();
        }
        return false;
    }
