    public void sendFileToZipStream(String userbaseDir, String fileStr, ZipOutputStream zipout) throws Exception {
        if (!userbaseDir.endsWith(sep)) {
            userbaseDir += sep;
        }
        BufferedInputStream in = null;
        byte dataBuff[] = new byte[bufferSize];
        String entryPath = fileStr;
        String entryFullPath = userbaseDir + entryPath;
        File sendFile = new File(entryFullPath);
        if ((sendFile.exists()) && (!sendFile.isDirectory())) {
            in = new BufferedInputStream(new FileInputStream(entryFullPath), bufferSize);
            ZipEntry zipEntry = new ZipEntry(entryPath);
            zipout.putNextEntry(zipEntry);
            int writeLen;
            while ((writeLen = in.read(dataBuff)) > 0) {
                zipout.write(dataBuff, 0, writeLen);
            }
            zipout.flush();
            zipout.closeEntry();
            in.close();
        } else {
            throw new Exception("file is not exist ! entryFullPath = (" + entryFullPath + ")");
        }
    }
