    boolean deleteAll(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) deleteAll(f);
        }
        return file.delete();
    }
