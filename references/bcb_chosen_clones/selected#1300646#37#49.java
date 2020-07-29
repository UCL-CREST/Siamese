        public void write(byte[] b, int off, int len) throws IOException {
            while (len > 2048) {
                write(b, off, 2048);
                off += 2048;
                len -= 2048;
            }
            if (len == 0) return;
            out.writeShort(Hamming16Code.generate(len - 1));
            out.write(b, off, len);
            CRC32 crc = new CRC32();
            crc.update(b, off, len);
            out.writeInt((int) crc.getValue());
        }
