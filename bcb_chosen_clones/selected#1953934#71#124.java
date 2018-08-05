    public static void zipDirectory(ZipOutputStream zipOutputStream, String zippedDir, String relativeDir, ProgressDialogJFrame progress) {
        if (progress != null) {
            progress.setProgressMessage(relativeDir);
        } else {
            logger.debug("  Zipping " + zippedDir + " # " + relativeDir);
        }
        File zipDir = new File(zippedDir);
        String[] dirList = zipDir.list();
        if (dirList.length == 0) {
            String entry = (relativeDir == null ? zippedDir : relativeDir + File.separator);
            ZipEntry ze = new ZipEntry(entry);
            try {
                zipOutputStream.putNextEntry(ze);
            } catch (IOException e) {
                logger.debug("Unable to zip empty directory " + entry, e);
            }
        } else {
            for (int i = 0; i < dirList.length; i++) {
                File file = new File(zippedDir, dirList[i]);
                if (file.isDirectory()) {
                    zipDirectory(zipOutputStream, file.getPath(), (relativeDir == null ? dirList[i] : relativeDir + File.separator + dirList[i]), progress);
                } else {
                    FileInputStream fis = null;
                    try {
                        byte[] readBuffer = new byte[2048];
                        int bytesReaded = 0;
                        String fileToZip = file.getPath();
                        String entry = (relativeDir == null ? dirList[i] : relativeDir + File.separator + dirList[i]);
                        if (progress != null) {
                            progress.setProgressMessage(entry);
                        } else {
                            logger.debug("  Adding file " + fileToZip + " # " + entry);
                        }
                        fis = new FileInputStream(fileToZip);
                        ZipEntry ze = new ZipEntry(entry);
                        zipOutputStream.putNextEntry(ze);
                        while ((bytesReaded = fis.read(readBuffer)) != -1) {
                            zipOutputStream.write(readBuffer, 0, bytesReaded);
                        }
                    } catch (Exception e) {
                        logger.debug("Unable to zip: " + e.getMessage(), e);
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (Exception ee) {
                                logger.debug("Unable to close stream!", ee);
                            }
                        }
                    }
                }
            }
        }
    }
