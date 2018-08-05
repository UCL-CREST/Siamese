    public static boolean deleteFile(File path) {
        if (path.exists()) {
            if (path.isDirectory()) {
                File[] files = path.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteFile(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (path.delete());
    }
