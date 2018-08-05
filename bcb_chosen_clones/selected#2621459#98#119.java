    public static boolean copyFile(final File fileFrom, final File fileTo) {
        assert fileFrom != null : "fileFrom is null";
        assert fileTo != null : "fileTo is null";
        LOGGER.info(buildLogString(COPY_FILE_INFO, new Object[] { fileFrom, fileTo }));
        boolean error = true;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(fileFrom);
            outputStream = new FileOutputStream(fileTo);
            final FileChannel inChannel = inputStream.getChannel();
            final FileChannel outChannel = outputStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            error = false;
        } catch (final IOException e) {
            LOGGER.log(SEVERE, buildLogString(COPY_FILE_ERROR, new Object[] { fileFrom, fileTo }), e);
        } finally {
            closeCloseable(inputStream, fileFrom);
            closeCloseable(outputStream, fileTo);
        }
        return error;
    }
