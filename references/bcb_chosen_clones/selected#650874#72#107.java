    public final void close() throws IOException {
        if (dataStream == null) throw new NullPointerException("Write stream is null.");
        dataStream.flush();
        dataStream.close();
        dataStream = null;
        File tmpFile = new File(packPath + ".tmp");
        FileOutputStream packStream = new FileOutputStream(packPath);
        try {
            String nbFiles = Long.toString(currentNbFiles) + "\0";
            packStream.write(FLAT_PACK_HEADER.getBytes(Charsets.ISO_8859_1));
            structBufferWriter.flush();
            structBufferWriter.close();
            int headerSize = structBuffer.size() + nbFiles.length();
            packStream.write(Integer.toString(headerSize).getBytes(Charsets.ISO_8859_1));
            packStream.write('\0');
            packStream.write(nbFiles.getBytes(Charsets.ISO_8859_1));
            structBuffer.writeTo(packStream);
            structBufferWriter = null;
            structBuffer = null;
            FileInputStream in = new FileInputStream(tmpFile);
            try {
                byte[] buffer = new byte[FILE_COPY_BUFFER_LEN];
                int read;
                while ((read = in.read(buffer)) > 0) packStream.write(buffer, 0, read);
                packStream.flush();
                packStream.close();
            } finally {
                Utilities.closeStream(in);
            }
        } finally {
            Utilities.closeStream(packStream);
        }
        if (tmpFile.isFile()) Utilities.deleteFile(tmpFile);
        packPath = null;
        structBuffer = null;
    }
