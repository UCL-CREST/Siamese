    static void clean(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) clean(f);
        }
        file.delete();
    }
