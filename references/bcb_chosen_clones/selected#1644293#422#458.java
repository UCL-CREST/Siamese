     *
     *@param  fileVec   �ļ���=(�ļ���,�ļ�����)
     *@param  fileName  ͬʱ��ѹ��������д�����ļ���
     *@return           ѹ���Ķ���������
     */
    public byte[] zip(Vector fileVec, String fileName) {
        byte[] BtBak = null;
        byte[] btSrc = null;
        java.io.ByteArrayOutputStream bao = null;
        java.util.zip.ZipEntry zipentry;
        ManageFile mfj = new ManageFile();
        int n;
        int i;
        Vector temp = fileVec;
        String tempFileName = null;
        String tempFileContent = null;
        java.util.zip.ZipOutputStream zipoutputstream = null;
        try {
            bao = new java.io.ByteArrayOutputStream();
            zipoutputstream = new java.util.zip.ZipOutputStream(bao);
            zipoutputstream.setMethod(java.util.zip.ZipOutputStream.DEFLATED);
            int len = temp.size();
            for (int j = 0; j < len; j++) {
                tempFileName = (String) ((Vector) temp.elementAt(j)).elementAt(0);
                tempFileContent = (String) ((Vector) temp.elementAt(j)).elementAt(1);
                zipentry = new java.util.zip.ZipEntry(tempFileName);
                zipentry.setSize(tempFileContent.getBytes().length);
                zipoutputstream.putNextEntry(zipentry);
                zipoutputstream.setLevel(getZipLevel());
                zipoutputstream.write(tempFileContent.getBytes());
                zipoutputstream.closeEntry();
            }
            zipoutputstream.close();
            mfj.DeleteFile(fileName, false);
            mfj.saveFile(fileName, bao.toByteArray());
            return bao.toByteArray();
        } catch (IOException ie) {
