
     *
     *@param  btSrc  Դ���

     *@return        ѹ�������

     *@roseuid       3C16C72101D0
     */
    public static synchronized byte[] zip(byte[] btSrc) {
        byte[] BtBak = null;
        java.io.ByteArrayOutputStream bao = null;
        int n;
        int i;
        try {
            bao = new java.io.ByteArrayOutputStream();
            java.util.zip.ZipOutputStream zipoutputstream = new java.util.zip.ZipOutputStream(bao);
            zipoutputstream.setMethod(java.util.zip.ZipOutputStream.DEFLATED);
            java.util.zip.ZipEntry zipentry = new java.util.zip.ZipEntry("srcFile.bin");
            zipentry.setSize(btSrc.length);
            zipoutputstream.putNextEntry(zipentry);
            zipoutputstream.setLevel(getZipLevel());
            zipoutputstream.write(btSrc);
