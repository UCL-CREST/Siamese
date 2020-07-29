    private void copyRecursive(File source, File destination, FileFilter filter) {
        if (source.isDirectory()) {
            boolean okay = destination.mkdir();
            if (!okay) {
                throw new FileCreationFailedException(destination.getAbsolutePath(), true);
            }
            File[] children;
            if (filter == null) {
                children = source.listFiles();
            } else {
                children = source.listFiles(filter);
            }
            for (File file : children) {
                copyRecursive(file, new File(destination, file.getName()), filter);
            }
        } else {
            copyFile(source, destination);
        }
    }
