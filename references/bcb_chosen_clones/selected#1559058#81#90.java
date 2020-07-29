    public String getID() {
        CRC32 checksum = new CRC32();
        int i = 0;
        checksum.reset();
        while ((i < data.length()) && (i < 5000)) {
            checksum.update((int) data.charAt(i));
            i++;
        }
        return (Long.toHexString(checksum.getValue()) + Integer.toHexString(data.hashCode()));
    }
