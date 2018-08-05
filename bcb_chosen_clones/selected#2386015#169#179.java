    private void copyZipEntry(InputStream oldBackupIn, ZipOutputStream oldBackupOut, ZipEntry e, byte[] prepend) throws IOException {
        ZipEntry eOut = new ZipEntry(e.getName());
        eOut.setTime(e.getTime());
        oldBackupOut.putNextEntry(eOut);
        if (prepend != null) oldBackupOut.write(prepend);
        int bytesRead;
        while ((bytesRead = oldBackupIn.read(copyBuf)) != -1) {
            oldBackupOut.write(copyBuf, 0, bytesRead);
        }
        oldBackupOut.closeEntry();
    }
