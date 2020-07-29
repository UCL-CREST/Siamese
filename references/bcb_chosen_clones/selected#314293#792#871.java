    public void adjustPadding(File file, int paddingSize, long audioStart) throws FileNotFoundException, IOException {
        logger.finer("Need to move audio file to accomodate tag");
        FileChannel fcIn = null;
        FileChannel fcOut;
        ByteBuffer paddingBuffer = ByteBuffer.wrap(new byte[paddingSize]);
        File paddedFile;
        try {
            paddedFile = File.createTempFile(Utils.getMinBaseFilenameAllowedForTempFile(file), ".new", file.getParentFile());
            logger.finest("Created temp file:" + paddedFile.getName() + " for " + file.getName());
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, ioe.getMessage(), ioe);
            if (ioe.getMessage().equals(FileSystemMessage.ACCESS_IS_DENIED.getMsg())) {
                logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
                throw new UnableToCreateFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
            } else {
                logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
                throw new UnableToCreateFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
            }
        }
        try {
            fcOut = new FileOutputStream(paddedFile).getChannel();
        } catch (FileNotFoundException ioe) {
            logger.log(Level.SEVERE, ioe.getMessage(), ioe);
            logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_TO_MODIFY_TEMPORARY_FILE_IN_FOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
            throw new UnableToModifyFileException(ErrorMessage.GENERAL_WRITE_FAILED_TO_MODIFY_TEMPORARY_FILE_IN_FOLDER.getMsg(file.getName(), file.getParentFile().getPath()));
        }
        try {
            fcIn = new FileInputStream(file).getChannel();
            long written = fcOut.write(paddingBuffer);
            logger.finer("Copying:" + (file.length() - audioStart) + "bytes");
            long audiolength = file.length() - audioStart;
            if (audiolength <= MAXIMUM_WRITABLE_CHUNK_SIZE) {
                long written2 = fcIn.transferTo(audioStart, audiolength, fcOut);
                logger.finer("Written padding:" + written + " Data:" + written2);
                if (written2 != audiolength) {
                    throw new RuntimeException(ErrorMessage.MP3_UNABLE_TO_ADJUST_PADDING.getMsg(audiolength, written2));
                }
            } else {
                long noOfChunks = audiolength / MAXIMUM_WRITABLE_CHUNK_SIZE;
                long lastChunkSize = audiolength % MAXIMUM_WRITABLE_CHUNK_SIZE;
                long written2 = 0;
                for (int i = 0; i < noOfChunks; i++) {
                    written2 += fcIn.transferTo(audioStart + (i * MAXIMUM_WRITABLE_CHUNK_SIZE), MAXIMUM_WRITABLE_CHUNK_SIZE, fcOut);
                }
                written2 += fcIn.transferTo(audioStart + (noOfChunks * MAXIMUM_WRITABLE_CHUNK_SIZE), lastChunkSize, fcOut);
                logger.finer("Written padding:" + written + " Data:" + written2);
                if (written2 != audiolength) {
                    throw new RuntimeException(ErrorMessage.MP3_UNABLE_TO_ADJUST_PADDING.getMsg(audiolength, written2));
                }
            }
            long lastModified = file.lastModified();
            if (fcIn != null) {
                if (fcIn.isOpen()) {
                    fcIn.close();
                }
            }
            if (fcOut != null) {
                if (fcOut.isOpen()) {
                    fcOut.close();
                }
            }
            replaceFile(file, paddedFile);
            paddedFile.setLastModified(lastModified);
        } finally {
            try {
                if (fcIn != null) {
                    if (fcIn.isOpen()) {
                        fcIn.close();
                    }
                }
                if (fcOut != null) {
                    if (fcOut.isOpen()) {
                        fcOut.close();
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Problem closing channels and locks:" + e.getMessage(), e);
            }
        }
    }
