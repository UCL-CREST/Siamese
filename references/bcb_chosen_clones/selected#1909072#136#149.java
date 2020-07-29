    public static boolean writeFileByBinary(InputStream pIs, File pFile, boolean pAppend) {
        boolean flag = false;
        try {
            FileOutputStream fos = new FileOutputStream(pFile, pAppend);
            IOUtils.copy(pIs, fos);
            fos.flush();
            fos.close();
            pIs.close();
            flag = true;
        } catch (Exception e) {
            LOG.error("将字节流写入�?" + pFile.getName() + "出现异常�?", e);
        }
        return flag;
    }
