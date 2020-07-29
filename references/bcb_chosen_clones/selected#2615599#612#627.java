    private void writeVariableHeader() throws SQLException {
        Data page = createData();
        page.writeInt(0);
        page.writeLong(writeCount);
        page.writeInt(logKey);
        page.writeInt(logFirstTrunkPage);
        page.writeInt(logFirstDataPage);
        CRC32 crc = new CRC32();
        crc.update(page.getBytes(), 4, pageSize - 4);
        page.setInt(0, (int) crc.getValue());
        file.seek(pageSize);
        file.write(page.getBytes(), 0, pageSize);
        file.seek(pageSize + pageSize);
        file.write(page.getBytes(), 0, pageSize);
        writeCount++;
    }
