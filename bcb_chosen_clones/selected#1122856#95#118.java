    public void addFile(File file, String objectId) throws DAException {
        try {
            String entryString = "";
            FileInputStream in = new FileInputStream(file.getAbsolutePath());
            System.out.println(" Adding: " + file.getAbsolutePath());
            if (objectId != null) {
                entryString = m_batchNumber + File.separator + OBJECT_DIR_PREFIX + objectId + File.separator + file.getName();
            } else {
                entryString = m_batchNumber + File.separator + file.getName();
            }
            m_zipOutputStream.putNextEntry(new ZipEntry(entryString));
            byte[] tmpBuf = new byte[1024];
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                m_zipOutputStream.write(tmpBuf, 0, len);
            }
            m_zipOutputStream.closeEntry();
            in.close();
        } catch (IOException ioEx) {
            DAException ex = new DAException(DAExceptionCodes.IO_ERROR, new String[] { ioEx.getMessage(), file.getAbsolutePath() });
            logger.log(DAExceptionCodes.IO_ERROR, this, "addFile", "Error while adding file to zip", ex);
            throw ex;
        }
    }
