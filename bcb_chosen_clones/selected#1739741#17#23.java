    public static long calculateCheckSum(CharSequence data) {
        final Checksum checksummer = new CRC32();
        for (int i = 0; i < data.length(); i++) {
            checksummer.update(data.charAt(i));
        }
        return checksummer.getValue();
    }
