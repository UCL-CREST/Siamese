    public static byte[] getBytesFromFile(File fileRef) throws IOException {
        FileInputStream file = null;
        ByteArrayOutputStream byteArrOut = null;
        try {
            file = new FileInputStream(fileRef.getPath());
            byteArrOut = new ByteArrayOutputStream();
            int ln;
            byte[] buf = new byte[1024 * 12];
            while ((ln = file.read(buf)) != -1) {
                byteArrOut.write(buf, 0, ln);
            }
            return byteArrOut.toByteArray();
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (file != null) {
                file.close();
            }
        }
    }
