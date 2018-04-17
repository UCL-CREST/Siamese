    public void adjustPadding(File file, int paddingSize, long audioStart) throws FileNotFoundException, IOException {
        logger.finer("Need to move audio file to accomodate tag");
        FileChannel fcIn;
        FileChannel fcOut;
        ByteBuffer paddingBuffer = ByteBuffer.wrap(new byte[paddingSize]);
        File paddedFile = File.createTempFile("temp", ".mp3", file.getParentFile());
        fcOut = new FileOutputStream(paddedFile).getChannel();
        fcIn = new FileInputStream(file).getChannel();
        long written = (long) fcOut.write(paddingBuffer);
        logger.finer("Copying:" + (file.length() - audioStart) + "bytes");
        long audiolength = file.length() - audioStart;
        if (audiolength <= MAXIMUM_WRITABLE_CHUNK_SIZE) {
            long written2 = fcIn.transferTo(audioStart, audiolength, fcOut);
            logger.finer("Written padding:" + written + " Data:" + written2);
            if (written2 != audiolength) {
                throw new RuntimeException("Problem adjusting padding, expecting to write:" + audiolength + ":only wrote:" + written2);
            }
        } else {
            long noOfChunks = audiolength / MAXIMUM_WRITABLE_CHUNK_SIZE;
            long lastChunkSize = audiolength % MAXIMUM_WRITABLE_CHUNK_SIZE;
            long written2 = 0;
            for (int i = 0; i < noOfChunks; i++) {
                written2 += fcIn.transferTo(audioStart + (i * MAXIMUM_WRITABLE_CHUNK_SIZE), MAXIMUM_WRITABLE_CHUNK_SIZE, fcOut);
                Runtime.getRuntime().gc();
            }
            written2 += fcIn.transferTo(audioStart + (noOfChunks * MAXIMUM_WRITABLE_CHUNK_SIZE), lastChunkSize, fcOut);
            logger.finer("Written padding:" + written + " Data:" + written2);
            if (written2 != audiolength) {
                throw new RuntimeException("Problem adjusting padding in large file, expecting to write:" + audiolength + ":only wrote:" + written2);
            }
        }
        long lastModified = file.lastModified();
        fcIn.close();
        fcOut.close();
        file.delete();
        paddedFile.renameTo(file);
        paddedFile.setLastModified(lastModified);
    }
