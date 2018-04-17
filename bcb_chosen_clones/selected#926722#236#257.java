    public void zipDocsetFiles(SaxHandler theXmlHandler, int theEventId, Attributes theAtts) throws BpsProcessException {
        ZipOutputStream myZipOut = null;
        BufferedInputStream myDocumentInputStream = null;
        String myFinalFile = null;
        String myTargetPath = null;
        String myTargetFileName = null;
        String myInputFileName = null;
        byte[] myBytesBuffer = null;
        int myLength = 0;
        try {
            myZipOut = new ZipOutputStream(new FileOutputStream(myFinalFile));
            myZipOut.putNextEntry(new ZipEntry(myTargetPath + myTargetFileName));
            myDocumentInputStream = new BufferedInputStream(new FileInputStream(myInputFileName));
            while ((myLength = myDocumentInputStream.read(myBytesBuffer, 0, 4096)) != -1) myZipOut.write(myBytesBuffer, 0, myLength);
            myZipOut.closeEntry();
            myZipOut.close();
        } catch (FileNotFoundException e) {
            throw (new BpsProcessException(BpsProcessException.ERR_OPEN_FILE, "FileNotFoundException while building zip dest file"));
        } catch (IOException e) {
            throw (new BpsProcessException(BpsProcessException.ERR_OPEN_FILE, "IOException while building zip dest file"));
        }
    }
