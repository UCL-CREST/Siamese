    public static void copyFolder(final File src, final File dest, final boolean recursive, final boolean onlyNew, final FileFilter filter) throws IOException {
        if (!src.isDirectory()) {
            throw new IOException(ResourceManager.getMessage(PACKAGE_NAME, "notAFolder", src));
        }
        if (dest.isFile()) {
            throw new IOException(ResourceManager.getMessage(PACKAGE_NAME, "isFile", dest));
        }
        if (!dest.exists() && !dest.mkdirs()) {
            throw new IOException(ResourceManager.getMessage(PACKAGE_NAME, "cantMakeFolder", dest));
        }
        File[] srcFiles = (filter != null) ? src.listFiles(filter) : src.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            File file = srcFiles[i];
            if (file.isDirectory()) {
                if (recursive) {
                    copyFolder(file, new File(dest, file.getName()), recursive, onlyNew, filter);
                }
                continue;
            }
            File destFile = new File(dest, file.getName());
            if (onlyNew && destFile.isFile() && (destFile.lastModified() > file.lastModified())) {
                continue;
            }
            copyFile(file, destFile);
        }
        dest.setLastModified(src.lastModified());
    }
