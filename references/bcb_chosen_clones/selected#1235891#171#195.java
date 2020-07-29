    public void zipBackupData(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        ZipEntry entry;
        try {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    entry = new ZipEntry(files[i].getPath() + "/");
                    zipFile.putNextEntry(entry);
                    zipBackupData(files[i].getPath());
                    continue;
                }
                FileInputStream in = new FileInputStream(files[i]);
                entry = new ZipEntry(path + "/" + files[i].getName());
                zipFile.putNextEntry(entry);
                int c;
                while ((c = in.read()) != -1) zipFile.write(c);
                zipFile.closeEntry();
                in.close();
            }
        } catch (Exception ex) {
            CampaignData.mwlog.errLog("Unable to backup server data files: " + path);
            CampaignData.mwlog.errLog(ex);
        }
    }
