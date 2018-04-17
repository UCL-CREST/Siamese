    private long getSeed() {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        try {
            crc.update(value.getBytes("UTF-8"));
            crc.update(getDefaultTitle().getBytes("UTF-8"));
        } catch (Exception e) {
            LogUtil.logError(logger, "UTF-8 doesn't exists", e);
            crc.update(value.getBytes());
            crc.update(getDefaultTitle().getBytes());
        }
        return crc.getValue();
    }
