    public static boolean delete(File file) {
        if (file.isFile()) {
            return file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File temp : files) {
                delete(temp);
            }
            return true;
        }
        return true;
    }
