    public void add(String name, InputStream is, String password) throws IOException, UnsupportedEncodingException {
        encrypter.init(password, 256);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(bos, new Deflater(9, true), 8 * 1024);
        int read = 0;
        long inputLen = 0;
        byte[] buf = new byte[8 * 1024];
        while ((read = is.read(buf)) > 0) {
            inputLen += read;
            dos.write(buf, 0, read);
        }
        dos.close();
        byte[] data = bos.toByteArray();
        ExtZipEntry entry = new ExtZipEntry(name);
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(inputLen);
        entry.setCompressedSize(data.length + 28);
        entry.setTime((new java.util.Date()).getTime());
        entry.initEncryptedEntry();
        zipOS.putNextEntry(entry);
        zipOS.writeBytes(encrypter.getSalt());
        zipOS.writeBytes(encrypter.getPwVerification());
        encrypter.encrypt(data, data.length);
        zipOS.writeBytes(data, 0, data.length);
        byte[] finalAuthentication = encrypter.getFinalAuthentication();
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("finalAuthentication=" + Arrays.toString(finalAuthentication) + " at pos=" + zipOS.getWritten());
        }
        zipOS.writeBytes(finalAuthentication);
    }
