    protected void truncate(File file) {
        LogLog.debug("Compression of file: " + file.getAbsolutePath() + " started.");
        if (FileUtils.isFileOlder(file, ManagementFactory.getRuntimeMXBean().getStartTime())) {
            File backupRoot = new File(getBackupDir());
            if (!backupRoot.exists() && !backupRoot.mkdirs()) {
                throw new AppenderInitializationError("Can't create backup dir for backup storage");
            }
            SimpleDateFormat df;
            try {
                df = new SimpleDateFormat(getBackupDateFormat());
            } catch (Exception e) {
                throw new AppenderInitializationError("Invalid date formate for backup files: " + getBackupDateFormat(), e);
            }
            String date = df.format(new Date(file.lastModified()));
            File zipFile = new File(backupRoot, file.getName() + "." + date + ".zip");
            ZipOutputStream zos = null;
            FileInputStream fis = null;
            try {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                ZipEntry entry = new ZipEntry(file.getName());
                entry.setMethod(ZipEntry.DEFLATED);
                entry.setCrc(FileUtils.checksumCRC32(file));
                zos.putNextEntry(entry);
                fis = FileUtils.openInputStream(file);
                byte[] buffer = new byte[1024];
                int readed;
                while ((readed = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, readed);
                }
            } catch (Exception e) {
                throw new AppenderInitializationError("Can't create zip file", e);
            } finally {
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        LogLog.warn("Can't close zip file", e);
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        LogLog.warn("Can't close zipped file", e);
                    }
                }
            }
            if (!file.delete()) {
                throw new AppenderInitializationError("Can't delete old log file " + file.getAbsolutePath());
            }
        }
    }
