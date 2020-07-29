    private void removeDirectory(File dir) throws IOException {
        if (!dir.isDirectory()) {
            return;
        }
        File entryList[] = dir.listFiles();
        for (File f : entryList) {
            if (f.isDirectory()) {
                removeDirectory(f);
            }
            if (f.isDirectory() || f.isFile()) {
                if (!f.delete()) {
                    throw new IOException("Failed to delete " + f);
                }
            }
        }
        if (!dir.delete()) {
            throw new IOException("Failed to delete " + dir);
        }
    }
