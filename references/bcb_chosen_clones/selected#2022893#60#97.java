    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }
        long time = System.currentTimeMillis();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size && continueWriting(pos, size)) {
                count = (size - pos) > FIFTY_MB ? FIFTY_MB : (size - pos);
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            output.close();
            IOUtils.closeQuietly(fos);
            input.close();
            IOUtils.closeQuietly(fis);
        }
        if (srcFile.length() != destFile.length()) {
            if (DiskManager.isLocked()) throw new IOException("Copy stopped since VtM was working"); else throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        } else {
            time = System.currentTimeMillis() - time;
            long speed = (destFile.length() / time) / 1000;
            DiskManager.addDiskSpeed(speed);
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }
