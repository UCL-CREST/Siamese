    public static void copyFromToDir(File source, File dest) throws IOException {
        if (source.isDirectory()) {
            if (!dest.mkdirs()) {
                String message = "Could not create directory " + dest;
                throw new IOException(message);
            }
        }
        File[] files = source.listFiles();
        if (null == files || files.length <= 0) return;
        for (int i = 0; i < files.length; i++) {
            File newfile = new File(dest, getLastPathComponent(files[i]));
            if (!files[i].isDirectory()) {
                copyFromToFile(files[i], newfile);
            } else {
                if (!newfile.mkdirs()) {
                    throw new IOException("Error copying directory, could not create directory " + newfile);
                }
                copyFromToDir(files[i], newfile);
            }
        }
    }
