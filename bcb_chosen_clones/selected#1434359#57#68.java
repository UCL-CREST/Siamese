    public static byte[] readBytesFromFile(File file) throws IOException {
        long fileLength = file.length();
        if (fileLength > Integer.MAX_VALUE) {
            String msg = "Files must be less than Integer.MAX_VALUE=" + Integer.MAX_VALUE + " in length." + " Found file.length()=" + file.length();
            throw new IllegalArgumentException(msg);
        }
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream((int) fileLength);
        Streams.copy(in, bytesOut);
        Streams.closeQuietly(in);
        return bytesOut.toByteArray();
    }
