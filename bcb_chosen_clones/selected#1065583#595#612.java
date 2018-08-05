    public void sendStringToZipStream(String userbaseDir, String fileStr, String fileValueString, ZipOutputStream zipout) throws Exception {
        if (!userbaseDir.endsWith(sep)) {
            userbaseDir += sep;
        }
        BufferedInputStream in = null;
        byte dataBuff[] = new byte[bufferSize];
        String entryPath = fileStr;
        in = new BufferedInputStream(new ByteArrayInputStream(fileValueString.getBytes()), bufferSize);
        ZipEntry zipEntry = new ZipEntry(entryPath);
        zipout.putNextEntry(zipEntry);
        int writeLen;
        while ((writeLen = in.read(dataBuff)) > 0) {
            zipout.write(dataBuff, 0, writeLen);
        }
        zipout.flush();
        zipout.closeEntry();
        in.close();
    }
