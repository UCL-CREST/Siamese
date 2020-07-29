    public Boolean compress(String sSourceDir, ArrayList<String> aFiles, String sDestinationFilename) {
        logger.debug("compress(%s, %s, %s)", sSourceDir, aFiles, sDestinationFilename);
        BufferedInputStream oOrigin = null;
        FileOutputStream oDestination;
        ZipOutputStream oOutput = null;
        Iterator<String> oIterator;
        byte[] aData;
        try {
            oDestination = new FileOutputStream(sDestinationFilename);
            oOutput = new ZipOutputStream(new BufferedOutputStream(oDestination));
            aData = new byte[BUFFER_SIZE];
            oIterator = aFiles.iterator();
            while (oIterator.hasNext()) {
                try {
                    String sFilename = (String) oIterator.next();
                    FileInputStream fisInput = new FileInputStream(sSourceDir + File.separator + sFilename);
                    oOrigin = new BufferedInputStream(fisInput, BUFFER_SIZE);
                    ZipEntry oEntry = new ZipEntry(sFilename.replace('\\', '/'));
                    oOutput.putNextEntry(oEntry);
                    int iCount;
                    while ((iCount = oOrigin.read(aData, 0, BUFFER_SIZE)) != -1) oOutput.write(aData, 0, iCount);
                } finally {
                    StreamHelper.close(oOrigin);
                }
            }
        } catch (Exception oException) {
            logger.error(oException.getMessage(), oException);
            return false;
        } finally {
            StreamHelper.close(oOutput);
        }
        return true;
    }
