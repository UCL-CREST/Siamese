    public static void createBackup() {
        String workspacePath = Workspace.INSTANCE.getWorkspace();
        if (workspacePath.length() == 0) return;
        workspacePath += "/";
        String backupPath = workspacePath + "Backup";
        File directory = new File(backupPath);
        if (!directory.exists()) directory.mkdirs();
        String dateString = DataUtils.DateAndTimeOfNowAsLocalString();
        dateString = dateString.replace(" ", "_");
        dateString = dateString.replace(":", "");
        backupPath += "/Backup_" + dateString + ".zip";
        ArrayList<String> backupedFiles = new ArrayList<String>();
        backupedFiles.add("Database/Database.properties");
        backupedFiles.add("Database/Database.script");
        FileInputStream in;
        byte[] data = new byte[1024];
        int read = 0;
        try {
            ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(backupPath));
            zip.setMethod(ZipOutputStream.DEFLATED);
            for (int i = 0; i < backupedFiles.size(); i++) {
                String backupedFile = backupedFiles.get(i);
                try {
                    File inFile = new File(workspacePath + backupedFile);
                    if (inFile.exists()) {
                        in = new FileInputStream(workspacePath + backupedFile);
                        if (in != null) {
                            ZipEntry entry = new ZipEntry(backupedFile);
                            zip.putNextEntry(entry);
                            while ((read = in.read(data, 0, 1024)) != -1) zip.write(data, 0, read);
                            zip.closeEntry();
                            in.close();
                        }
                    }
                } catch (Exception e) {
                    Logger.logError(e, "Error during file backup:" + backupedFile);
                }
            }
            zip.close();
        } catch (IOException ex) {
            Logger.logError(ex, "Error during backup");
        }
    }
