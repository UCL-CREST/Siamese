    private long getCRC(InputStream stream) {
        CRC32 crc = new CRC32();
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                crc.update(buffer, 0, length);
            }
        } catch (IOException e) {
            IdeLog.logError(SyncingPlugin.getDefault(), "Error retrieving CRC", e);
        }
        return crc.getValue();
    }
