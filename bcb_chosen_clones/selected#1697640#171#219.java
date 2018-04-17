    public static void copy(File from, File to, CopyMode mode) throws IOException {
        if (!from.exists()) {
            IllegalArgumentException e = new IllegalArgumentException("Source doesn't exist: " + from.getCanonicalFile());
            log.throwing("IOUtils", "copy", e);
            throw e;
        }
        if (from.isFile()) {
            if (!to.canWrite()) {
                IllegalArgumentException e = new IllegalArgumentException("Cannot write to target location: " + to.getCanonicalFile());
                log.throwing("IOUtils", "copy", e);
                throw e;
            }
        }
        if (to.exists()) {
            if ((mode.val & CopyMode.OverwriteFile.val) != CopyMode.OverwriteFile.val) {
                IllegalArgumentException e = new IllegalArgumentException("Target already exists: " + to.getCanonicalFile());
                log.throwing("IOUtils", "copy", e);
                throw e;
            }
            if (to.isDirectory()) {
                if ((mode.val & CopyMode.OverwriteFolder.val) != CopyMode.OverwriteFolder.val) {
                    IllegalArgumentException e = new IllegalArgumentException("Target is a folder: " + to.getCanonicalFile());
                    log.throwing("IOUtils", "copy", e);
                    throw e;
                } else to.delete();
            }
        }
        if (from.isFile()) {
            FileChannel in = new FileInputStream(from).getChannel();
            FileLock inLock = in.lock();
            FileChannel out = new FileOutputStream(to).getChannel();
            FileLock outLock = out.lock();
            try {
                in.transferTo(0, (int) in.size(), out);
            } finally {
                inLock.release();
                outLock.release();
                in.close();
                out.close();
            }
        } else {
            to.mkdirs();
            File[] contents = to.listFiles();
            for (File file : contents) {
                File newTo = new File(to.getCanonicalPath() + "/" + file.getName());
                copy(file, newTo, mode);
            }
        }
    }
