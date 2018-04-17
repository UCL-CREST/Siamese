    private void makeDailyBackup() throws CacheOperationException, ConfigurationException {
        final int MAX_DAILY_BACKUPS = 5;
        File cacheFolder = getBackupFolder();
        cacheLog.debug("Making a daily backup of current Beehive archive...");
        try {
            File oldestDaily = new File(DAILY_BACKUP_PREFIX + "." + MAX_DAILY_BACKUPS);
            if (oldestDaily.exists()) {
                moveToWeeklyBackup(oldestDaily);
            }
            for (int index = MAX_DAILY_BACKUPS - 1; index > 0; index--) {
                File daily = new File(cacheFolder, DAILY_BACKUP_PREFIX + "." + index);
                File target = new File(cacheFolder, DAILY_BACKUP_PREFIX + "." + (index + 1));
                if (!daily.exists()) {
                    cacheLog.debug("Daily backup file ''{0}'' was not present. Skipping...", daily.getAbsolutePath());
                    continue;
                }
                if (!daily.renameTo(target)) {
                    sortBackups();
                    throw new CacheOperationException("There was an error moving ''{0}'' to ''{1}''.", daily.getAbsolutePath(), target.getAbsolutePath());
                } else {
                    cacheLog.debug("Moved " + daily.getAbsolutePath() + " to " + target.getAbsolutePath());
                }
            }
        } catch (SecurityException e) {
            throw new ConfigurationException("Security Manager has denied read/write access to daily backup files in ''{0}'' : {1}" + e, cacheFolder.getAbsolutePath(), e.getMessage());
        }
        File beehiveArchive = getCachedArchive();
        File tempBackupArchive = new File(cacheFolder, BEEHIVE_ARCHIVE_NAME + ".tmp");
        BufferedInputStream archiveReader = null;
        BufferedOutputStream tempBackupWriter = null;
        try {
            archiveReader = new BufferedInputStream(new FileInputStream(beehiveArchive));
            tempBackupWriter = new BufferedOutputStream(new FileOutputStream(tempBackupArchive));
            int len, bytecount = 0;
            final int BUFFER_SIZE = 4096;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = archiveReader.read(buffer, 0, BUFFER_SIZE)) != -1) {
                tempBackupWriter.write(buffer, 0, len);
                bytecount += len;
            }
            tempBackupWriter.flush();
            long originalFileSize = beehiveArchive.length();
            if (originalFileSize != bytecount) {
                throw new CacheOperationException("Original archive size was {0} bytes but only {1} were copied.", originalFileSize, bytecount);
            }
            cacheLog.debug("Finished copying ''{0}'' to ''{1}''.", beehiveArchive.getAbsolutePath(), tempBackupArchive.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new CacheOperationException("Files required for copying a backup of Beehive archive could not be found, opened " + "or created : {1}", e, e.getMessage());
        } catch (IOException e) {
            throw new CacheOperationException("Error while making a copy of the Beehive archive : {0}", e, e.getMessage());
        } finally {
            if (archiveReader != null) {
                try {
                    archiveReader.close();
                } catch (Throwable t) {
                    cacheLog.warn("Failed to close stream to ''{0}'' : {1}", t, beehiveArchive.getAbsolutePath(), t.getMessage());
                }
            }
            if (tempBackupWriter != null) {
                try {
                    tempBackupWriter.close();
                } catch (Throwable t) {
                    cacheLog.warn("Failed to close stream to ''{0}'' : {1}", t, tempBackupArchive.getAbsolutePath(), t.getMessage());
                }
            }
        }
        validateArchive(tempBackupArchive);
        File newestDaily = getNewestDailyBackupFile();
        try {
            if (!tempBackupArchive.renameTo(newestDaily)) {
                throw new CacheOperationException("Error moving ''{0}'' to ''{1}''.", tempBackupArchive.getAbsolutePath(), newestDaily.getAbsolutePath());
            } else {
                cacheLog.info("Backup complete. Saved in ''{0}''", newestDaily.getAbsolutePath());
            }
        } catch (SecurityException e) {
            throw new ConfigurationException("Security Manager has denied write access to ''{0}'' : {1}", e, newestDaily.getAbsolutePath(), e.getMessage());
        }
    }
