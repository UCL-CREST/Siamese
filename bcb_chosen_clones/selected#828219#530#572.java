    private void buildExportZipFile(String parentDirectory, ZipOutputStream zipOutputStream, File[] files, int step) {
        try {
            for (int i = 0; i < files.length; i++) {
                exportProgressDialog.setBarValue(step++);
                if (files[i].isDirectory()) {
                    if (parentDirectory.equals("")) {
                        ZipEntry newZipEntry = new ZipEntry(files[i].getName() + "/");
                        zipOutputStream.putNextEntry(newZipEntry);
                        zipOutputStream.closeEntry();
                        zipOutputStream.flush();
                        File[] subFiles = files[i].listFiles();
                        buildExportZipFile(files[i].getName(), zipOutputStream, subFiles, step);
                    } else {
                        ZipEntry newZipEntry = new ZipEntry(parentDirectory + System.getProperty("file.separator") + files[i].getName() + "/");
                        zipOutputStream.putNextEntry(newZipEntry);
                        zipOutputStream.closeEntry();
                        zipOutputStream.flush();
                        File[] subFiles = files[i].listFiles();
                        buildExportZipFile(parentDirectory + System.getProperty("file.separator") + files[i].getName(), zipOutputStream, subFiles, step);
                    }
                } else {
                    ZipEntry newZipEntry;
                    if (parentDirectory.equals("")) {
                        newZipEntry = new ZipEntry(files[i].getName());
                    } else {
                        newZipEntry = new ZipEntry(parentDirectory + System.getProperty("file.separator") + files[i].getName());
                    }
                    zipOutputStream.putNextEntry(newZipEntry);
                    InputStream is = new FileInputStream(files[i]);
                    byte[] buf = new byte[4096];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        zipOutputStream.write(buf, 0, len);
                    }
                    is.close();
                    zipOutputStream.closeEntry();
                    zipOutputStream.flush();
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
