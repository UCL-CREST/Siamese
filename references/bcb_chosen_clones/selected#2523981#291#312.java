    public static boolean delTree(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; files != null && i < files.length; i++) {
                    if (!delTree(files[i])) {
                        return false;
                    }
                }
            }
            if (!file.delete()) {
                return false;
            } else {
                if (file.exists()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
