    public static final byte[] zipBuffer(byte[] buffer) {
        if (buffer == null) {
            throw new RuntimeException("buffer is null, zipBuffer");
        }
        MLUtil.d("zipBuffer: " + buffer.length);
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        ByteArrayOutputStream os = new ByteArrayOutputStream(1000);
        ZipOutputStream zip = new ZipOutputStream(os);
        zip.setMethod(ZipOutputStream.DEFLATED);
        try {
            ZipEntry zipentry = new ZipEntry("A");
            zipentry.setSize(buffer.length);
            zip.putNextEntry(zipentry);
            int n;
            byte[] temp = new byte[TEMP_FILE_BUFFER_SIZE];
            while ((n = is.read(temp)) > -1) {
                zip.write(temp, 0, n);
            }
            zip.closeEntry();
            zip.close();
            is.close();
            os.close();
            byte[] bb = os.toByteArray();
            MLUtil.d("zipBuffer-compressed=" + bb.length);
            return (bb);
        } catch (IOException e) {
            MLUtil.runtimeError(e, "zipBuffer");
        }
        return (null);
    }
