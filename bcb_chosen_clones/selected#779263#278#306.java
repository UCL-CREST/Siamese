    @Override
    public void copy(final String fileName) throws FileIOException {
        try {
            if (opened) {
                fileChannel.position(0);
            } else {
                fileChannel = new FileInputStream(file).getChannel();
            }
            FileChannel dstChannel = null;
            try {
                dstChannel = new FileOutputStream(fileName).getChannel();
                dstChannel.transferFrom(fileChannel, 0, fileChannel.size());
            } finally {
                try {
                    if (dstChannel != null) {
                        dstChannel.close();
                    }
                } catch (Exception exception) {
                }
            }
            if (opened) {
                fileChannel.position(currentPositionInFile);
            } else {
                fileChannel.close();
            }
        } catch (IOException exception) {
            throw HELPER_FILE_UTIL.fileIOException("failed copy " + file + " to " + fileName, null, exception);
        }
    }
