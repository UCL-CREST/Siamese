    public static void synchronizeFolders(final File src, final File dest, final FileFilter filter) throws IOException {
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
        for (File srcFile : srcFiles) {
            File destFile = new File(dest, srcFile.getName());
            if (srcFile.isDirectory()) {
                if (destFile.isFile() && !destFile.delete()) {
                    throw new IOException(ResourceManager.getMessage(PACKAGE_NAME, "cantDeleteFile", destFile));
                }
                synchronizeFolders(srcFile, destFile, filter);
                continue;
            }
            if (compareFiles(srcFile, destFile)) {
                continue;
            }
            copyFile(srcFile, destFile);
        }
        File[] destFiles = dest.listFiles();
        for (int i = 0; i < destFiles.length; i++) {
            File destFile = destFiles[i];
            File srcFile = new File(src, destFile.getName());
            if (((filter != null) && filter.accept(destFile) && srcFile.exists()) || ((filter == null) && srcFile.exists())) {
                continue;
            }
            if (destFile.isDirectory() && !emptyFolder(destFile)) {
                throw new IOException(ResourceManager.getMessage(PACKAGE_NAME, "cantEmptyFolder", destFile));
            }
            if (!destFile.delete()) {
                throw new IOException(ResourceManager.getMessage(PACKAGE_NAME, "cantDeleteFile", destFile));
            }
        }
        dest.setLastModified(src.lastModified());
    }
