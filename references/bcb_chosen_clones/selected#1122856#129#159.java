    public void addDir(File dirObj) throws DAException {
        try {
            if (!dirObj.isDirectory()) {
                DAException ex = new DAException(DAExceptionCodes.IO_ERROR, new String[] { "Input is not a directory" });
                logger.log(this, "addDir", null, ex);
                throw ex;
            }
            File[] files = dirObj.listFiles();
            byte[] tmpBuf = new byte[1024];
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    addDir(files[i]);
                    continue;
                }
                FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
                System.out.println(" Adding: " + files[i].getAbsolutePath());
                m_zipOutputStream.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
                int len;
                while ((len = in.read(tmpBuf)) > 0) {
                    m_zipOutputStream.write(tmpBuf, 0, len);
                }
                m_zipOutputStream.closeEntry();
                in.close();
            }
            m_zipOutputStream.close();
        } catch (IOException ioExcp) {
            DAException ex = new DAException(DAExceptionCodes.IO_ERROR, new String[] { ioExcp.getMessage() });
            logger.log(DAExceptionCodes.IO_ERROR, this, "addDir", "Error while adding file to zip", ex);
            throw ex;
        }
    }
