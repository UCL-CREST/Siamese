    public static boolean writeFileB2C(InputStream pIs, File pFile, boolean pAppend) {
        boolean flag = false;
        try {
            FileWriter fw = new FileWriter(pFile, pAppend);
            IOUtils.copy(pIs, fw);
            fw.flush();
            fw.close();
            pIs.close();
            flag = true;
        } catch (Exception e) {
            LOG.error("将字节流写入�?" + pFile.getName() + "出现异常�?", e);
        }
        return flag;
    }
