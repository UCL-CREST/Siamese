    boolean delete(File file) throws SecurityException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File element : files) {
                if (!delete(element)) {
                    return false;
                }
            }
        }
        return file.delete();
    }
