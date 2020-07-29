    private void backupFile(ZipEntry oldEntry, ZipInputStream oldBackupIn, ZipOutputStream oldBackupOut, ZipOutputStream newBackupOut, File file, String filename) throws IOException {
        ByteArrayOutputStream bytesSeen = null;
        InputStream oldIn = null;
        if (oldEntry != null && oldBackupIn != null && oldBackupOut != null) {
            bytesSeen = new ByteArrayOutputStream();
            oldIn = oldBackupIn;
        }
        ZipEntry e = new ZipEntry(filename);
        e.setTime(file.lastModified());
        e.setSize(file.length());
        newBackupOut.putNextEntry(e);
        InputStream fileIn = new BufferedInputStream(new FileInputStream(file));
        OutputStream fileOut = newBackupOut;
        int c, d;
        try {
            while ((c = fileIn.read()) != -1) {
                fileOut.write(c);
                if (oldIn != null) {
                    d = oldIn.read();
                    if (d != -1) bytesSeen.write(d);
                    if (c != d) {
                        copyZipEntry(oldIn, oldBackupOut, oldEntry, bytesSeen.toByteArray());
                        oldIn = null;
                        bytesSeen = null;
                        oldBackupIn = null;
                        oldBackupOut = null;
                        wroteEntryToOldBackup(filename);
                    }
                }
                ThreadThrottler.tick();
            }
        } finally {
            fileIn.close();
        }
        if (oldIn != null) {
            d = oldIn.read();
            if (d != -1) {
                bytesSeen.write(d);
                copyZipEntry(oldIn, oldBackupOut, oldEntry, bytesSeen.toByteArray());
                wroteEntryToOldBackup(filename);
            }
        }
        fileOut.flush();
        newBackupOut.closeEntry();
    }
