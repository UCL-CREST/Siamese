    public static synchronized void copyDirectory(File source, File destination) throws BlogunityException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(source, children[i]), new File(destination, children[i]));
            }
        } else {
            copyFile(source, destination);
        }
    }
