    private void readHeader() throws IOException {
        CRC32 headCRC = new CRC32();
        int magic = in.read();
        if (magic < 0) {
            eos = true;
            return;
        }
        int magic2 = in.read();
        if ((magic + (magic2 << 8)) != GZIP_MAGIC) throw new IOException("Error in GZIP header, bad magic code");
        headCRC.update(magic);
        headCRC.update(magic2);
        int CM = in.read();
        if (CM != Deflater.DEFLATED) throw new IOException("Error in GZIP header, data not in deflate format");
        headCRC.update(CM);
        int flags = in.read();
        if (flags < 0) throw new EOFException("Early EOF in GZIP header");
        headCRC.update(flags);
        if ((flags & 0xd0) != 0) throw new IOException("Reserved flag bits in GZIP header != 0");
        for (int i = 0; i < 6; i++) {
            int readByte = in.read();
            if (readByte < 0) throw new EOFException("Early EOF in GZIP header");
            headCRC.update(readByte);
        }
        if ((flags & FEXTRA) != 0) {
            for (int i = 0; i < 2; i++) {
                int readByte = in.read();
                if (readByte < 0) throw new EOFException("Early EOF in GZIP header");
                headCRC.update(readByte);
            }
            if (in.read() < 0 || in.read() < 0) throw new EOFException("Early EOF in GZIP header");
            int len1, len2, extraLen;
            len1 = in.read();
            len2 = in.read();
            if ((len1 < 0) || (len2 < 0)) throw new EOFException("Early EOF in GZIP header");
            headCRC.update(len1);
            headCRC.update(len2);
            extraLen = (len1 << 8) | len2;
            for (int i = 0; i < extraLen; i++) {
                int readByte = in.read();
                if (readByte < 0) throw new EOFException("Early EOF in GZIP header");
                headCRC.update(readByte);
            }
        }
        if ((flags & FNAME) != 0) {
            int readByte;
            while ((readByte = in.read()) > 0) headCRC.update(readByte);
            if (readByte < 0) throw new EOFException("Early EOF in GZIP file name");
            headCRC.update(readByte);
        }
        if ((flags & FCOMMENT) != 0) {
            int readByte;
            while ((readByte = in.read()) > 0) headCRC.update(readByte);
            if (readByte < 0) throw new EOFException("Early EOF in GZIP comment");
            headCRC.update(readByte);
        }
        if ((flags & FHCRC) != 0) {
            int tempByte;
            int crcval = in.read();
            if (crcval < 0) throw new EOFException("Early EOF in GZIP header");
            tempByte = in.read();
            if (tempByte < 0) throw new EOFException("Early EOF in GZIP header");
            crcval = (crcval << 8) | tempByte;
            if (crcval != ((int) headCRC.getValue() & 0xffff)) throw new IOException("Header CRC value mismatch");
        }
        readGZIPHeader = true;
    }
