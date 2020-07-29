    public static void deleteDir(File dir) throws IOException {
        if (!dir.isDirectory() || !dir.canWrite()) throw new IOException("Not a directory, or not writable: " + dir.getAbsolutePath());
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) throw new IOException("Directory " + dir.getAbsolutePath() + " contains other directories; not deleted.");
        }
        for (File f : dir.listFiles()) {
            f.delete();
        }
        dir.delete();
    }
