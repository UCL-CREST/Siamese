    public static boolean writeFileByChars(Reader pReader, File pFile, boolean pAppend) {
        boolean flag = false;
        try {
            FileWriter fw = new FileWriter(pFile, pAppend);
            IOUtils.copy(pReader, fw);
            fw.flush();
            fw.close();
            pReader.close();
            flag = true;
        } catch (Exception e) {
            LOG.error("将字符流写入�?" + pFile.getName() + "出现异常�?", e);
        }
        return flag;
    }
