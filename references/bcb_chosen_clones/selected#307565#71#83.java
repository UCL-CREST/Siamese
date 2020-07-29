    public static boolean deleteTree(File start) {
        assert start != null;
        if (!start.exists()) return false;
        if (start.isFile()) return start.delete();
        assert start.isDirectory();
        File[] children = start.listFiles();
        boolean succ = true;
        for (File child : children) {
            succ = deleteTree(child) && succ;
        }
        succ = start.delete() && succ;
        return succ;
    }
