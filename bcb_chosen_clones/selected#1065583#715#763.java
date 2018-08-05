    public void sendJobOutputDirPidFilesToZipStream(String userbaseDir, String FilesDirStr, ZipOutputStream zipout, String outputLogType, String pidID) throws Exception {
        System.out.println(userbaseDir + FilesDirStr);
        if (!userbaseDir.endsWith(sep)) {
            userbaseDir += sep;
        }
        if (!FilesDirStr.endsWith(sep)) {
            FilesDirStr += sep;
        }
        BufferedInputStream in = null;
        byte dataBuff[] = new byte[bufferSize];
        File filesDir = new File(userbaseDir + FilesDirStr + ".");
        if ((filesDir.exists()) && (filesDir.isDirectory())) {
            String fileList[] = filesDir.list();
            if (fileList.length > 0) {
                for (int pos = 0; pos < fileList.length; pos++) {
                    String entry = fileList[pos];
                    String entryPath = FilesDirStr + entry;
                    String entryFullPath = userbaseDir + entryPath;
                    if (new File(entryFullPath).isFile()) {
                        boolean sendFileToZip = true;
                        if (FileUtils.getInstance().isLogFileName(entry)) {
                            if ("none".equals(outputLogType)) {
                                sendFileToZip = false;
                            }
                        }
                        if (FileUtils.getInstance().isLinkFile(entryFullPath)) {
                            sendFileToZip = false;
                        }
                        if (sendFileToZip) {
                            String entry2 = FileUtils.getInstance().getNormalFromPidName(entry);
                            String entryPath2 = FilesDirStr + entry2;
                            in = new BufferedInputStream(new FileInputStream(entryFullPath), bufferSize);
                            ZipEntry zipEntry = new ZipEntry(entryPath2);
                            zipout.putNextEntry(zipEntry);
                            int writeLen;
                            while ((writeLen = in.read(dataBuff)) > 0) {
                                zipout.write(dataBuff, 0, writeLen);
                            }
                            zipout.flush();
                            zipout.closeEntry();
                            in.close();
                        }
                    }
                }
            } else {
            }
        } else {
        }
    }
