    @Deprecated
    public boolean backupLuceneIndex(int indexLocation, int backupLocation) {
        boolean result = false;
        try {
            System.out.println("lucene backup started");
            String indexPath = this.getIndexFolderPath(indexLocation);
            String backupPath = this.getIndexFolderPath(backupLocation);
            File inDir = new File(indexPath);
            boolean flag = true;
            if (inDir.exists() && inDir.isDirectory()) {
                File filesList[] = inDir.listFiles();
                if (filesList != null) {
                    File parDirBackup = new File(backupPath);
                    if (!parDirBackup.exists()) parDirBackup.mkdir();
                    String date = this.getDate();
                    backupPath += "/" + date;
                    File dirBackup = new File(backupPath);
                    if (!dirBackup.exists()) dirBackup.mkdir(); else {
                        File files[] = dirBackup.listFiles();
                        if (files != null) {
                            for (int i = 0; i < files.length; i++) {
                                if (files[i] != null) {
                                    files[i].delete();
                                }
                            }
                        }
                        dirBackup.delete();
                        dirBackup.mkdir();
                    }
                    for (int i = 0; i < filesList.length; i++) {
                        if (filesList[i].isFile()) {
                            try {
                                File destFile = new File(backupPath + "/" + filesList[i].getName());
                                if (!destFile.exists()) destFile.createNewFile();
                                FileInputStream in = new FileInputStream(filesList[i]);
                                FileOutputStream out = new FileOutputStream(destFile);
                                FileChannel fcIn = in.getChannel();
                                FileChannel fcOut = out.getChannel();
                                fcIn.transferTo(0, fcIn.size(), fcOut);
                            } catch (FileNotFoundException ex) {
                                System.out.println("FileNotFoundException ---->" + ex);
                                flag = false;
                            } catch (IOException excIO) {
                                System.out.println("IOException ---->" + excIO);
                                flag = false;
                            }
                        }
                    }
                }
            }
            System.out.println("lucene backup finished");
            System.out.println("flag ========= " + flag);
            if (flag) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("Exception in backupLuceneIndex Method : " + e);
            e.printStackTrace();
        }
        return result;
    }
