    public static final void copyDirectory(File source, File destination) throws IOException {
        if (!source.isDirectory()) {
            throw new IllegalArgumentException("Source (" + source.getPath() + ") must be a directory.");
        }
        if (!source.exists()) {
            throw new IllegalArgumentException("Source directory (" + source.getPath() + ") doesn't exist.");
        }
        if (destination.exists()) {
            throw new IllegalArgumentException("Destination (" + destination.getPath() + ") exists.");
        }
        destination.mkdirs();
        File[] files = source.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                copyDirectory(file, new File(destination, file.getName()));
            } else {
                copyFile(file, new File(destination, file.getName()));
            }
        }
    }
