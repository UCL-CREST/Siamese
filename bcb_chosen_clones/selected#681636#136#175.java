    public void saveTo(File file) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);
            {
                ZipEntry certEntry = new ZipEntry(UserZipFile.PUBLIC_CERTIFICATE_NAME);
                byte[] certBytes = getCertificate().getEncoded();
                certEntry.setSize(certBytes.length);
                zos.putNextEntry(certEntry);
                zos.write(certBytes);
            }
            {
                ZipEntry keyEntry = new ZipEntry(UserZipFile.PRIVATE_KEY_NAME);
                byte[] keyBytes = getPrivateKey().getEncoded();
                keyEntry.setSize(keyBytes.length);
                zos.putNextEntry(keyEntry);
                zos.write(keyBytes);
            }
            zos.finish();
            bos.flush();
            fos.flush();
            zos.close();
            bos.close();
            fos.close();
            if (getPassphrase() != null) {
                File encrypted = SecurityUtil.encryptDiskBacked(passphrase, file);
                IOUtil.renameFallbackCopy(encrypted, file);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't save the user information to " + file.getPath() + ": " + e.getMessage(), e);
        } finally {
            IOUtil.safeClose(zos);
            IOUtil.safeClose(bos);
            IOUtil.safeClose(fos);
        }
    }
