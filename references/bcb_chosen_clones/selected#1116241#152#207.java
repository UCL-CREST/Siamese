    public static void copyDirectoryRecursively(File source, File target, boolean overwrite, FileFilter fileFilter, boolean temporary) throws IOException {
        assert source != null;
        assert target != null;
        final FileFilter filter = fileFilter != null ? fileFilter : AllFileFilter.getInstance();
        if (source.equals(target)) {
            return;
        }
        if (!source.isDirectory()) {
            throw new IOException("Couldn't copy, the source '" + source.getAbsolutePath() + "' is not a directory");
        }
        File[] acceptedFiles = source.listFiles(filter);
        File[] leftChildFolders = source.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return !filter.accept(pathname) && DirectoryFileFilter.getInstance().accept(pathname);
            }
        });
        if (acceptedFiles.length > 0 && !target.exists() && !target.mkdirs()) {
            throw new IOException("Couldn't create target directory: " + target.getAbsolutePath());
        }
        for (int i = 0; i < acceptedFiles.length; i++) {
            if (acceptedFiles[i].isDirectory()) {
                File newTarget = new File(target.getPath(), acceptedFiles[i].getName());
                if (temporary) {
                    newTarget.deleteOnExit();
                }
                copyDirectoryRecursively(acceptedFiles[i], newTarget, overwrite, AllFileFilter.getInstance(), temporary);
            } else if (acceptedFiles[i].isFile()) {
                File src = acceptedFiles[i].getAbsoluteFile();
                File dst = new File(target + File.separator + acceptedFiles[i].getName());
                if (src.equals(dst)) {
                    continue;
                }
                if (dst.exists()) {
                    if (overwrite) {
                        dst.delete();
                    } else {
                        continue;
                    }
                }
                if (temporary) {
                    dst.deleteOnExit();
                }
                FileUtilities.copyBinaryFile(src, dst);
            } else {
                throw new IOException(acceptedFiles[i] + "' isn't file or directory, what else?");
            }
        }
        for (int i = 0; i < leftChildFolders.length; i++) {
            File newTarget = new File(target.getPath(), leftChildFolders[i].getName());
            if (temporary) {
                newTarget.deleteOnExit();
            }
            copyDirectoryRecursively(leftChildFolders[i], newTarget, overwrite, filter, temporary);
        }
    }
