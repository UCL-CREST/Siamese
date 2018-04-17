    public String backUp(LinkedList<String> fileBackupList, String pathTo, String groupName, String groupId) {
        if (!pathTo.endsWith("\\")) {
            groupId = pathTo.substring(pathTo.lastIndexOf("\\") + 1, pathTo.lastIndexOf("."));
            pathTo = pathTo.substring(0, pathTo.lastIndexOf("\\") + 1);
        }
        backUp = new Backup(groupName, groupId);
        if (!groupId.endsWith(".bk")) {
            groupId += ".bk";
        }
        File zipFile = new File(pathTo + groupId);
        ZipOutputStream zos;
        try {
            if (zipFile.exists()) {
                File tempFile = File.createTempFile(zipFile.getName(), null);
                tempFile.delete();
                zipFile.renameTo(tempFile);
                ZipInputStream zis = new ZipInputStream(new FileInputStream(tempFile));
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                ZipEntry entry = zis.getNextEntry();
                while (entry != null) {
                    if (entry.getName().equals(FILE_INFO)) {
                        entry = zis.getNextEntry();
                        continue;
                    } else {
                        zos.putNextEntry(new ZipEntry(entry.getName()));
                        int length;
                        while ((length = zis.read(BUFFER)) > 0) {
                            zos.write(BUFFER, 0, length);
                        }
                        entry = zis.getNextEntry();
                    }
                }
                zis.close();
                backUp.execute(fileBackupList, zos);
                BackUpInfoFileGroup fileGroup = importData(tempFile);
                for (int i = 0; i < backUp.getFileGroup().getFileList().size(); i++) {
                    fileGroup.getFileList().add(backUp.getFileGroup().getFileList().get(i));
                }
                fileGroup.setSize(fileGroup.getSize() + backUp.getFileGroup().getSize());
                if (exportData == null) {
                    exportData = new ExportData();
                }
                exportData.execute(fileGroup, zos, FILE_INFO);
            } else {
                zos = new ZipOutputStream(new FileOutputStream(pathTo + groupId));
                backUp.execute(fileBackupList, zos);
                if (exportData == null) {
                    exportData = new ExportData();
                }
                exportData.execute(backUp.getFileGroup(), zos, FILE_INFO);
            }
            zos.close();
            return groupId;
        } catch (FileNotFoundException e) {
            throw new BackupException(e.getMessage());
        } catch (IOException e) {
            throw new BackupException(e.getMessage());
        }
    }
