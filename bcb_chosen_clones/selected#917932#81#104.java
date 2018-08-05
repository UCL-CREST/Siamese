    protected void add(ZipEntry zipEntry, ZipFileEntryInputStream zipData, String password) throws IOException, UnsupportedEncodingException {
        AESEncrypter aesEncrypter = new AESEncrypterBC(password.getBytes("iso-8859-1"));
        ExtZipEntry entry = new ExtZipEntry(zipEntry.getName());
        entry.setMethod(zipEntry.getMethod());
        entry.setSize(zipEntry.getSize());
        entry.setCompressedSize(zipEntry.getCompressedSize() + 28);
        entry.setTime(zipEntry.getTime());
        entry.initEncryptedEntry();
        zipOS.putNextEntry(entry);
        zipOS.writeBytes(aesEncrypter.getSalt());
        zipOS.writeBytes(aesEncrypter.getPwVerification());
        byte[] data = new byte[1024];
        int read = zipData.read(data);
        while (read != -1) {
            aesEncrypter.encrypt(data, read);
            zipOS.writeBytes(data, 0, read);
            read = zipData.read(data);
        }
        byte[] finalAuthentication = aesEncrypter.getFinalAuthentication();
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("finalAuthentication=" + Arrays.toString(finalAuthentication) + " at pos=" + zipOS.getWritten());
        }
        zipOS.writeBytes(finalAuthentication);
    }
