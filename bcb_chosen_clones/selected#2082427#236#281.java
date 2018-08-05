    @Override
    public void copy(final String fileName) throws FileIOException {
        final long savedCurrentPositionInFile = currentPositionInFile;
        if (opened) {
            closeImpl();
        }
        final FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            throw HELPER_FILE_UTIL.fileIOException(FAILED_OPEN + file, file, exception);
        }
        final File destinationFile = new File(fileName);
        final FileOutputStream fos;
        try {
            fos = new FileOutputStream(destinationFile);
        } catch (FileNotFoundException exception) {
            throw HELPER_FILE_UTIL.fileIOException(FAILED_OPEN + destinationFile, destinationFile, exception);
        }
        try {
            final byte[] buf = new byte[1024];
            int readLength = 0;
            while ((readLength = fis.read(buf)) != -1) {
                fos.write(buf, 0, readLength);
            }
        } catch (IOException exception) {
            throw HELPER_FILE_UTIL.fileIOException("failed copy from " + file + " to " + destinationFile, null, exception);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception exception) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception exception) {
            }
        }
        if (opened) {
            openImpl();
            seek(savedCurrentPositionInFile);
        }
    }
