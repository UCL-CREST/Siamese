    private void writeEnd(DataOutputStream out) throws IOException {
        stream.finish();
        byte[] dataArray = new byte[buffer.size() + 4];
        dataArray[0] = 'I';
        dataArray[1] = 'D';
        dataArray[2] = 'A';
        dataArray[3] = 'T';
        System.arraycopy(buffer.toByteArray(), 0, dataArray, 4, buffer.size());
        out.writeInt(dataArray.length - 4);
        out.write(dataArray);
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(dataArray);
        out.writeInt((int) crc.getValue());
        byte[] end = createEnd();
        out.writeInt(end.length - 4);
        out.write(end);
        crc = new CRC32();
        crc.reset();
        crc.update(end);
        out.writeInt((int) crc.getValue());
        return;
    }
