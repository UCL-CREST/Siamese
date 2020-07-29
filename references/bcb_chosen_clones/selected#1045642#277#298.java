    public void createZIPFileBackup(ArrayList<File> filesToZip) {
        try {
            byte[] buffer = new byte[1024];
            File zipFile = getNewFile(new File(Data.DATA_FOLDER_PATH + System.currentTimeMillis() + "added_files_backup.zip"));
            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);
            for (File a : filesToZip) {
                log.log(Level.INFO, this.getClass().getName(), "Added file " + a.getAbsolutePath() + " to zip file " + zipFile.getAbsolutePath());
                FileInputStream fin = new FileInputStream(a);
                zout.putNextEntry(new ZipEntry(a.getName()));
                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
                fin.close();
            }
            zout.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, this.getClass().getName(), e);
        }
    }
