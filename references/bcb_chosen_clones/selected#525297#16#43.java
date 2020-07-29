    static void copyFile(File file, File destDir) {
        File destFile = new File(destDir, file.getName());
        if (destFile.exists() && (!destFile.canWrite())) {
            throw new SyncException("Cannot overwrite " + destFile + " because " + "it is read-only");
        }
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                FileOutputStream out = new FileOutputStream(destFile);
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new SyncException("I/O error copying " + file + " to " + destDir + " (message: " + e.getMessage() + ")", e);
        }
        if (!destFile.setLastModified(file.lastModified())) {
            throw new SyncException("Could not set last modified timestamp " + "of " + destFile);
        }
    }
