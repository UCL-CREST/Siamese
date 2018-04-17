    public static void copyFile(File source, File dest) throws IOException {
        log.debug("Copy from {} to {}", source.getAbsoluteFile(), dest.getAbsoluteFile());
        FileInputStream fi = new FileInputStream(source);
        FileChannel fic = fi.getChannel();
        MappedByteBuffer mbuf = fic.map(FileChannel.MapMode.READ_ONLY, 0, source.length());
        fic.close();
        fi.close();
        fi = null;
        if (!dest.exists()) {
            String destPath = dest.getPath();
            log.debug("Destination path: {}", destPath);
            String destDir = destPath.substring(0, destPath.lastIndexOf(File.separatorChar));
            log.debug("Destination dir: {}", destDir);
            File dir = new File(destDir);
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    log.debug("Directory created");
                } else {
                    log.warn("Directory not created");
                }
            }
            dir = null;
        }
        FileOutputStream fo = new FileOutputStream(dest);
        FileChannel foc = fo.getChannel();
        foc.write(mbuf);
        foc.close();
        fo.close();
        fo = null;
        mbuf.clear();
        mbuf = null;
    }
