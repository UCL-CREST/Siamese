    public static int fileCopy(String strSourceFilePath, String strDestinationFilePath, String strFileName) throws IOException {
        String SEPARATOR = System.getProperty("file.separator");
        File dir = new File(strSourceFilePath);
        if (!dir.exists()) dir.mkdirs();
        File realDir = new File(strDestinationFilePath);
        if (!realDir.exists()) realDir.mkdirs();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(new File(strSourceFilePath + SEPARATOR + strFileName));
            fos = new FileOutputStream(new File(strDestinationFilePath + SEPARATOR + strFileName));
            IOUtils.copy(fis, fos);
        } catch (Exception ex) {
            return -1;
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (Exception ex2) {
            }
        }
        return 0;
    }
