    private void delete(File f) {
        if (f.exists() && f.isDirectory()) {
            File[] entries = f.listFiles();
            for (int i = 0; i < entries.length; i++) {
                delete(entries[i]);
            }
            f.delete();
        } else {
            f.delete();
        }
    }
