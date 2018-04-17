    public void zipBackupPlayers() {
        File folder = new File("./campaign/players");
        File[] files = folder.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getName());
                zipFile.putNextEntry(entry);
                int c;
                while ((c = in.read()) != -1) zipFile.write(c);
                zipFile.closeEntry();
                in.close();
            }
        } catch (Exception ex) {
            CampaignData.mwlog.errLog("Unable to backup player files");
            CampaignData.mwlog.errLog(ex);
        }
    }
