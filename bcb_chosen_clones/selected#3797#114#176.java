    public void extractEntry(ExtZipEntry zipEntry, File outFile, String password) throws IOException, ZipException, DataFormatException {
        if (zipEntry == null) {
            throw new ZipException("zipEntry must NOT be NULL");
        }
        if (zipEntry.isEncrypted()) {
            byte[] pwBytes = password.getBytes(charset);
            CentralDirectoryEntry cde = zipEntry.getCentralDirectoryEntry();
            int cryptoHeaderOffset = zipEntry.getOffset() - cde.getCryptoHeaderLength();
            byte[] salt = raFile.readByteArray(cryptoHeaderOffset, 16);
            byte[] pwVerification = raFile.readByteArray(cryptoHeaderOffset + 16, 2);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("\n" + cde.toString());
                LOG.finest("offset    = " + zipEntry.getOffset());
                LOG.finest("cryptoOff = " + cryptoHeaderOffset);
                LOG.finest("pwBytes   = " + ByteArrayHelper.toString(pwBytes) + " - " + pwBytes.length);
                LOG.finest("salt      = " + ByteArrayHelper.toString(salt) + " - " + salt.length);
                LOG.finest("pwVerif   = " + ByteArrayHelper.toString(pwVerification) + " - " + pwVerification.length);
            }
            AESDecrypter decrypter = new AESDecrypterBC(pwBytes, salt, pwVerification);
            File tmpFile = new File(outFile.getPath() + "_TMP.zip");
            makeDir(new File(tmpFile.getParent()));
            ExtZipOutputStream zos = new ExtZipOutputStream(tmpFile);
            ExtZipEntry tmpEntry = new ExtZipEntry(zipEntry);
            tmpEntry.setPrimaryCompressionMethod(zipEntry.getMethod());
            zos.putNextEntry(tmpEntry);
            raFile.seek(cde.getOffset());
            byte[] buffer = new byte[bufferSize];
            int remaining = (int) zipEntry.getEncryptedDataSize();
            while (remaining > 0) {
                int len = (remaining > buffer.length) ? buffer.length : remaining;
                int read = raFile.readByteArray(buffer, len);
                decrypter.decrypt(buffer, read);
                zos.writeBytes(buffer, 0, read);
                remaining -= len;
            }
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
            ZipFile zf = new ZipFile(tmpFile);
            ZipEntry ze = zf.entries().nextElement();
            InputStream is = zf.getInputStream(ze);
            FileOutputStream fos = new FileOutputStream(outFile.getPath());
            int read = is.read(buffer);
            while (read > 0) {
                fos.write(buffer, 0, read);
                read = is.read(buffer);
            }
            fos.close();
            is.close();
            zf.close();
            tmpFile.delete();
        } else {
            throw new ZipException("currently only extracts encrypted files - use java.util.zip to unzip");
        }
    }
