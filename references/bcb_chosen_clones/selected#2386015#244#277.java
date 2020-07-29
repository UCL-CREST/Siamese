    private void writeHistLogFile(ZipInputStream oldBackupIn, ZipOutputStream newBackupOut, File dataDir) throws IOException {
        File currentLog = new File(dataDir, LOG_FILE_NAME);
        if (oldBackupIn == null && !currentLog.exists()) return;
        ZipEntry e = new ZipEntry(HIST_LOG_FILE_NAME);
        e.setTime(System.currentTimeMillis());
        newBackupOut.putNextEntry(e);
        byte[] histLog = null;
        if (oldBackupIn != null) {
            histLog = FileUtils.slurpContents(oldBackupIn, false);
            long totalSize = histLog.length + currentLog.length();
            int skip = (int) Math.max(0, totalSize - maxHistLogSize);
            if (skip < histLog.length) newBackupOut.write(histLog, skip, histLog.length - skip); else histLog = null;
        }
        if (currentLog.exists() && currentLog.length() > 0) {
            InputStream currentLogIn = new BufferedInputStream(new FileInputStream(currentLog));
            try {
                byte[] currLogStart = new byte[100];
                int matchLen = currentLogIn.read(currLogStart);
                int lastLogEntryPos = findLastLogEntryStart(histLog);
                if (matches(histLog, lastLogEntryPos, currLogStart, 0, matchLen)) {
                    int duplicateLen = histLog.length - lastLogEntryPos;
                    int skip = duplicateLen - matchLen;
                    if (skip > 0) currentLogIn.skip(skip);
                } else {
                    newBackupOut.write(HIST_SEPARATOR.getBytes());
                    newBackupOut.write(currLogStart, 0, matchLen);
                }
                FileUtils.copyFile(currentLogIn, newBackupOut);
            } finally {
                currentLogIn.close();
            }
        }
        newBackupOut.closeEntry();
    }
