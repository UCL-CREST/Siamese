    public static void compressFile(final File source, final File destination) {
        byte[] ioBuffer = new byte[8192];
        FileInputStream in = null;
        FileLock fosLock;
        FileLock fisLock = null;
        try {
            FileOutputStream fos = new FileOutputStream(destination);
            fosLock = fos.getChannel().tryLock();
            in = new FileInputStream(source);
            fisLock = in.getChannel().tryLock(0, Long.MAX_VALUE, true);
            if (fosLock != null && fisLock != null) {
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                zipOut.setLevel(Deflater.BEST_COMPRESSION);
                zipOut.putNextEntry(new ZipEntry(source.getName()));
                int length;
                while ((length = in.read(ioBuffer)) > 0) {
                    zipOut.write(ioBuffer, 0, length);
                }
                zipOut.closeEntry();
                fosLock.release();
                zipOut.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    if (fisLock != null) {
                        fisLock.release();
                    }
                    in.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
