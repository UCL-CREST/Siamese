    public void extractEntry(ExtZipEntry zipEntry, OutputStream outStream, String password) throws IOException, ZipException, DataFormatException {
        checkZipEntry(zipEntry);
        ZipInputStream zipInputStream = null;
        ByteArrayOutputStream bos = null;
        try {
            CentralDirectoryEntry cde = zipEntry.getCentralDirectoryEntry();
            if (!cde.isAesEncrypted()) {
                throw new ZipException("only AES encrypted files are supported");
            }
            int cryptoHeaderOffset = zipEntry.getOffset() - cde.getCryptoHeaderLength();
            byte[] salt = raFile.readByteArray(cryptoHeaderOffset, 16);
            byte[] pwVerification = raFile.readByteArray(cryptoHeaderOffset + 16, 2);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("\n" + cde.toString());
                LOG.finest("offset    = " + zipEntry.getOffset());
                LOG.finest("cryptoOff = " + cryptoHeaderOffset);
                LOG.finest("password  = " + password + " - " + password.length());
                LOG.finest("salt      = " + ByteArrayHelper.toString(salt) + " - " + salt.length);
                LOG.finest("pwVerif   = " + ByteArrayHelper.toString(pwVerification) + " - " + pwVerification.length);
            }
            decrypter.init(password, 256, salt, pwVerification);
            bos = new ByteArrayOutputStream(bufferSize);
            ExtZipOutputStream zos = new ExtZipOutputStream(bos);
            ExtZipEntry tmpEntry = new ExtZipEntry(zipEntry);
            tmpEntry.setPrimaryCompressionMethod(zipEntry.getMethod());
            tmpEntry.setCompressedSize(zipEntry.getEncryptedDataSize());
            zos.putNextEntry(tmpEntry);
            raFile.seek(cde.getOffset());
            byte[] buffer = new byte[bufferSize];
            CRC32 crc32 = new CRC32();
            int remaining = (int) zipEntry.getEncryptedDataSize();
            while (remaining > 0) {
                int len = (remaining > buffer.length) ? buffer.length : remaining;
                int read = raFile.readByteArray(buffer, len);
                decrypter.decrypt(buffer, read);
                zos.writeBytes(buffer, 0, read);
                remaining -= len;
                crc32.update(buffer, 0, read);
            }
            tmpEntry.setCrc(crc32.getValue());
            zos.finish();
            byte[] storedMac = new byte[10];
            raFile.readByteArray(storedMac, 10);
            byte[] calcMac = decrypter.getFinalAuthentication();
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("storedMac=" + Arrays.toString(storedMac));
                LOG.fine("calcMac=" + Arrays.toString(calcMac));
            }
            if (!Arrays.equals(storedMac, calcMac)) {
                throw new ZipException("stored authentication (mac) value does not match calculated one");
            }
            zipInputStream = new ZipInputStream(new ByteArrayInputStream(bos.toByteArray()));
            ZipEntry entry = zipInputStream.getNextEntry();
            entry.setCrc(crc32.getValue());
            if (entry.getSize() != 0) {
                crc32 = new CRC32();
                int read = zipInputStream.read(buffer);
                while (read > 0) {
                    outStream.write(buffer, 0, read);
                    crc32.update(buffer, 0, read);
                    entry.setCrc(crc32.getValue());
                    read = zipInputStream.read(buffer);
                }
            }
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (zipInputStream != null) {
                zipInputStream.close();
            }
        }
    }
