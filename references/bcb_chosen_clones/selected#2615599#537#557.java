    private void readVariableHeader() throws SQLException {
        Data page = createData();
        for (int i = 1; ; i++) {
            if (i == 3) {
                throw Message.getSQLException(ErrorCode.FILE_CORRUPTED_1, fileName);
            }
            page.reset();
            readPage(i, page);
            CRC32 crc = new CRC32();
            crc.update(page.getBytes(), 4, pageSize - 4);
            int expected = (int) crc.getValue();
            int got = page.readInt();
            if (expected == got) {
                writeCount = page.readLong();
                logKey = page.readInt();
                logFirstTrunkPage = page.readInt();
                logFirstDataPage = page.readInt();
                break;
            }
        }
    }
