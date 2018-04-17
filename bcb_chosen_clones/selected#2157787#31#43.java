    public static void deleteAll(File f) {
        if (f != null) {
            File[] toDels = f.listFiles();
            if (toDels != null) {
                for (int i = 0; i < toDels.length; i++) {
                    File todel = toDels[i];
                    if (todel.isDirectory()) deleteAll(todel);
                    todel.delete();
                }
            }
        }
        f.delete();
    }
