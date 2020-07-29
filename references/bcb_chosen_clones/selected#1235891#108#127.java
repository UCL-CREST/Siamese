    public void zipBackupFactions() {
        File folder = new File("./campaign/factions");
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            try {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getName());
                zipFile.putNextEntry(entry);
                int c;
                while ((c = in.read()) != -1) zipFile.write(c);
                zipFile.closeEntry();
                in.close();
            } catch (FileNotFoundException fnfe) {
                CampaignData.mwlog.errLog("Unable to backup faction file: " + files[i].getName());
            } catch (Exception ex) {
                CampaignData.mwlog.errLog("Unable to backup faction files");
                CampaignData.mwlog.errLog(ex);
            }
        }
    }
