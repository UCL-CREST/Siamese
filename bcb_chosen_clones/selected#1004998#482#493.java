    private boolean delete(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            boolean result = true;
            for (File child : children) {
                result &= delete(child);
            }
            return result && file.delete();
        } else {
            return file.delete();
        }
    }
