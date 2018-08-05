    public static boolean removeFiles(String sDir, String sFilter) {
        boolean bRet = true;
        try {
            File file = new File(sDir);
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    int idx = fileList[i].getName().lastIndexOf(sFilter);
                    if (idx > 0) {
                        if (fileList[i].delete()) log.info(fileList[i].getAbsolutePath() + " is deleted"); else log.info("failed to delete file " + fileList[i].getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            bRet = false;
            log.error(e);
        }
        return bRet;
    }
