        public int calculateCRC() {
            byte[] b = new byte[bStd.length - 2 + ((bExt != null) ? bExt.length : 0)];
            for (int i = 2; i < bStd.length; i++) {
                b[i - 2] = bStd[i];
            }
            if (bExt != null) {
                int j = bStd.length - 2;
                for (int i = 0; i < bExt.length; i++) {
                    b[j + i] = bExt[i];
                }
            }
            CRC32 crc = new CRC32();
            crc.reset();
            crc.update(b);
            return (short) (crc.getValue());
        }
