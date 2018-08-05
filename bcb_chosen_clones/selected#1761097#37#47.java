    protected void deleteRecursive(SmbFile file) throws IOException {
        if (!file.exists()) throw new IOException("The file does not exist.");
        if (!file.isDirectory()) throw new IOException("A file cannot be deleted recursively.");
        if (file.exists()) {
            SmbFile[] files = file.listFiles();
            for (SmbFile currentFile : files) {
                if (currentFile.isDirectory()) deleteRecursive(currentFile); else currentFile.delete();
            }
            file.delete();
        }
    }
