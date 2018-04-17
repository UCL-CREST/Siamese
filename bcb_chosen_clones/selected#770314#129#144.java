    public static void deleteAll(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            deleteAll(files[i]);
                        }
                        files[i].delete();
                    }
                }
            }
            file.delete();
        }
    }
