        public int calculateCRC() {
            CRC32 crc = new CRC32();
            crc.reset();
            crc.update(getData(), 2, getData().length - 2);
            return (short) (crc.getValue());
        }
