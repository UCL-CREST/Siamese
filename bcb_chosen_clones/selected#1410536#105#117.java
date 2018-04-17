    void output(OutputStream out) throws IOException {
        CRC32 crc = new CRC32();
        byte[] dataBytes = data.toByteArray();
        byte[] lenBytes = intToBytes(dataBytes.length);
        out.write(lenBytes);
        for (int i = 0; i < 4; i++) {
            crc.update(type.charAt(i));
            out.write(type.charAt(i));
        }
        crc.update(dataBytes);
        out.write(dataBytes);
        out.write(intToBytes((int) crc.getValue()));
    }
