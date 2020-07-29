    private long getSeed() {
        String s = dir.getAbsolutePath();
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        try {
            crc.update(s.getBytes("UTF-8"));
        } catch (Exception e) {
            LogUtil.logError(logger, "UTF-8 doesn't exists", e);
            crc.update(s.getBytes());
        }
        return crc.getValue();
    }
