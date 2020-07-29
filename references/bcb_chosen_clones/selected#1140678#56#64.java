    protected void clearDir(File dir) {
        File[] entries = dir.listFiles();
        for (int i = 0; i < entries.length; i++) {
            File entry = entries[i];
            if (!entry.exists()) continue;
            if (entry.isDirectory()) clearDir(entry);
            entry.delete();
        }
    }
