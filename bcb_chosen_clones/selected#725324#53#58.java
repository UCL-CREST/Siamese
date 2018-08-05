    static void deleteFile(File f, boolean toDeleteSelf) {
        if (f.isDirectory()) {
            for (File child : f.listFiles()) deleteFile(child, true);
        }
        if (toDeleteSelf) f.delete();
    }
