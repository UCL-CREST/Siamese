    public void update(BackUpInfoFileGroup backedUpFiles, BackUpInfoFileGroup updateFiles, String filePath) {
        if (update == null) {
            update = new Update();
        }
        try {
            File zipFile = new File(filePath);
            File tempFile = File.createTempFile(zipFile.getName(), null);
            tempFile.delete();
            zipFile.renameTo(tempFile);
            ZipInputStream zis = new ZipInputStream(new FileInputStream(tempFile));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (entry.getName().equals(FILE_INFO)) {
                    entry = zis.getNextEntry();
                    continue;
                } else if (!update.isToUpdate(updateFiles, entry.getName())) {
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    int len;
                    while ((len = zis.read(BUFFER)) > 0) {
                        zos.write(BUFFER, 0, len);
                    }
                }
                entry = zis.getNextEntry();
            }
            zis.close();
            update.execute(updateFiles, zos);
            if (exportData == null) {
                exportData = new ExportData();
            }
            exportData.execute(update.updateInformationFile(backedUpFiles, updateFiles), zos, FILE_INFO);
            zos.close();
        } catch (IOException e) {
            throw new BackupException(e.getMessage());
        }
    }
