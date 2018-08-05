    public static void deleteFolderContent(File folder) throws IOException {
        File[] fs = folder.listFiles();
        if (Utils.isEmptyArray(fs)) {
            return;
        }
        for (int i = 0; i < fs.length; i++) {
            File f = fs[i];
            if (!f.canWrite()) {
                throw new IOException("No write access to " + f.getAbsolutePath());
            }
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFolderContent(f);
                f.delete();
            }
        }
    }
