    private boolean zipSelectedFilesToOutputStream(String[] selectedFiles, OutputStream os, SessionContainer sessionContainer) throws Exception {
        boolean erfolg = true;
        byte[] buf = new byte[4096];
        int len = 0;
        long now = (new Date()).getTime();
        ByteArrayInputStream bais = null;
        FtpClientConnection ftpClientConnection = sessionContainer.getFtpClientConnection();
        ftpClientConnection.verifyConnection(sessionContainer.getLoginData());
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(Deflater.BEST_COMPRESSION);
        for (int ww = 0; ww < selectedFiles.length; ww++) {
            String actRemoteFile = this.assembleRemoteFileName(sessionContainer, selectedFiles[ww]);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            erfolg = ftpClientConnection.retrieveFile(actRemoteFile, baos);
            baos.close();
            byte[] fileContent = baos.toByteArray();
            if (!erfolg) {
                return (false);
            }
            CRC32 crc32 = new CRC32();
            bais = new ByteArrayInputStream(fileContent);
            while ((len = bais.read(buf)) > 0) {
                crc32.update(buf, 0, len);
            }
            bais.close();
            ZipEntry zipEntry = new ZipEntry(selectedFiles[ww]);
            zipEntry.setSize(fileContent.length);
            zipEntry.setTime(now);
            zipEntry.setCrc(crc32.getValue());
            zos.putNextEntry(zipEntry);
            bais = new ByteArrayInputStream(fileContent);
            while ((len = bais.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            bais.close();
            zos.closeEntry();
        }
        zos.finish();
        return (erfolg);
    }
