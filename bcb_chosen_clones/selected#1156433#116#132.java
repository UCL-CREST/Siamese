    public static boolean removeDirectory(File dir) {
        boolean rv = false;
        if (dir != null && dir.exists() && dir.isDirectory()) {
            for (File cur : dir.listFiles()) {
                if (cur.isDirectory()) removeDirectory(cur);
                try {
                    cur.delete();
                } catch (Throwable t) {
                }
            }
            try {
                dir.delete();
            } catch (Throwable t) {
            }
        }
        return rv;
    }
