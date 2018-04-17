    public static void copyFile(final File fromFile, File toFile) throws IOException {
        try {
            if (!fromFile.exists()) {
                throw new IOException("FileCopy: " + "no such source file: " + fromFile.getAbsoluteFile());
            }
            if (!fromFile.isFile()) {
                throw new IOException("FileCopy: " + "can't copy directory: " + fromFile.getAbsoluteFile());
            }
            if (!fromFile.canRead()) {
                throw new IOException("FileCopy: " + "source file is unreadable: " + fromFile.getAbsoluteFile());
            }
            if (toFile.isDirectory()) {
                toFile = new File(toFile, fromFile.getName());
            }
            if (toFile.exists() && !toFile.canWrite()) {
                throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFile.getAbsoluteFile());
            }
            final FileChannel inChannel = new FileInputStream(fromFile).getChannel();
            final FileChannel outChannel = new FileOutputStream(toFile).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } catch (final IOException e) {
                throw e;
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        } catch (final IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("CopyFile went wrong!", e);
            }
        }
    }
