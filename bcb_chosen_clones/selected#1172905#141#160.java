    private long getCRC(List<ErazeMethod> methods) {
        byte[] randomBytes = { (byte) 0x00, (byte) 0x00 };
        byte[] sizeByte = new byte[2];
        int patternLength;
        CRC32 crc = new CRC32();
        for (ErazeMethod method : methods) {
            for (PatternDefinition pattern : method.getPatterns()) {
                if (pattern.isRandom()) {
                    crc.update(randomBytes);
                } else {
                    patternLength = pattern.getPattern().length;
                    sizeByte[1] = (byte) ((patternLength >> 8) & 0xFF);
                    sizeByte[0] = (byte) ((patternLength) & 0xFF);
                    crc.update(sizeByte);
                    crc.update(pattern.getPattern());
                }
            }
        }
        return crc.getValue();
    }
