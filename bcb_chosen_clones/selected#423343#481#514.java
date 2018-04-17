    protected int writeNotesDir(ZipOutputStream out, File parentDir, File currentDir, ProgressMonitor pm, int progress) throws IOException {
        File[] entries = currentDir.listFiles();
        byte[] buffer = new byte[4096];
        int bytes_read;
        for (int i = 0; i < entries.length; i++) {
            if (pm.isCanceled()) {
                return 0;
            }
            File f = entries[i];
            if (f.isDirectory()) {
                progress = writeNotesDir(out, parentDir, f, pm, progress);
            } else {
                FileInputStream in = new FileInputStream(f);
                try {
                    String parentPath = parentDir.getParentFile().getAbsolutePath();
                    ZipEntry entry = new ZipEntry(f.getAbsolutePath().substring(parentPath.length() + 1));
                    out.putNextEntry(entry);
                    while ((bytes_read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytes_read);
                    }
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                }
                progress++;
            }
        }
        pm.setProgress(progress);
        return progress;
    }
