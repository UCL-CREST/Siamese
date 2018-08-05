    public static Boolean compress(String sSourceDir, ArrayList<String> aFiles, String sDestinationFilename) {
        log.info("PentahoReport() sourceDir: " + sSourceDir + " destination:" + sDestinationFilename);
        BufferedInputStream oOrigin = null;
        FileOutputStream oDestination;
        ZipOutputStream oOutput;
        Iterator<String> oIterator;
        byte[] aData;
        try {
            oDestination = new FileOutputStream(sDestinationFilename);
            oOutput = new ZipOutputStream(new BufferedOutputStream(oDestination));
            aData = new byte[BUFFER_SIZE];
            oIterator = aFiles.iterator();
            while (oIterator.hasNext()) {
                String sFilename = (String) oIterator.next();
                FileInputStream fisInput = new FileInputStream(sSourceDir + File.separator + sFilename);
                oOrigin = new BufferedInputStream(fisInput, BUFFER_SIZE);
                ZipEntry oEntry = new ZipEntry(sFilename.replace('\\', '/'));
                oOutput.putNextEntry(oEntry);
                int iCount;
                while ((iCount = oOrigin.read(aData, 0, BUFFER_SIZE)) != -1) {
                    oOutput.write(aData, 0, iCount);
                }
                oOrigin.close();
            }
            oOutput.close();
        } catch (Exception oException) {
            log.error(oException.getMessage());
            oException.printStackTrace();
            return false;
        }
        return true;
    }
