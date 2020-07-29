    public void update() throws IOException {
        if (!is_changed) return;
        boolean usesUnsynchronization = false;
        byte[] newFramesBytes = convertFramesToArrayOfBytes();
        int crc = 0;
        if (use_crc) {
            java.util.zip.CRC32 crcCalculator = new java.util.zip.CRC32();
            crcCalculator.update(newFramesBytes);
            crc = (int) crcCalculator.getValue();
        }
        ID3v2ExtendedHeader newExtHeader = new ID3v2ExtendedHeader(use_crc, crc, 0);
        byte[] newExtHeaderBytes = newExtHeader.getBytes();
        if (use_unsynchronization) {
            byte[] unsynchExtHeader = unsynchronize(newExtHeaderBytes);
            if (unsynchExtHeader != null) {
                usesUnsynchronization = true;
                newExtHeaderBytes = unsynchExtHeader;
            }
            byte[] unsynchFrames = unsynchronize(newFramesBytes);
            if (unsynchFrames != null) {
                usesUnsynchronization = true;
                newFramesBytes = unsynchFrames;
            }
        }
        int newHeaderLen = newExtHeaderBytes.length + newFramesBytes.length;
        ID3v2Header newHeader = new ID3v2Header(VERSION, REVISION, usesUnsynchronization, true, false, newHeaderLen);
        byte[] newHeaderBytes = newHeader.getBytes();
        int oldHeaderLen = 0;
        if (header == null) {
            oldHeaderLen = 0;
        } else {
            oldHeaderLen = header.getTagSize() + 10;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(newHeaderBytes);
        baos.write(newExtHeaderBytes);
        baos.write(newFramesBytes);
        long contentLen = file.length() - oldHeaderLen;
        long newFileLength = contentLen + newHeaderLen;
        if (use_padding) {
            long padding = 0;
            if (oldHeaderLen > newHeaderLen) {
                padding = oldHeaderLen - newHeaderLen;
            }
            for (long i = 0; i < padding; i++) baos.write(0);
        }
        byte[] finalNewHeader = baos.toByteArray();
        int minBuffSize = Math.abs(finalNewHeader.length - oldHeaderLen);
        int buffSize = Math.max(2048, minBuffSize);
        byte[] buf = new byte[buffSize];
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            long oldPointer = oldHeaderLen == 0 ? 0 : oldHeaderLen - 1;
            long newPointer = 0;
            raf.seek(oldPointer);
            long amountToRead = contentLen;
            if (amountToRead < buffSize) throw new IOException("Bad File");
            int read = raf.read(buf, 0, buffSize);
            amountToRead -= read;
            oldPointer = raf.getFilePointer();
            raf.seek(newPointer);
            raf.write(finalNewHeader);
            newPointer = raf.getFilePointer();
            while (amountToRead > 0) {
                byte[] buf2 = new byte[buffSize];
                raf.seek(oldPointer);
                read = raf.read(buf2, 0, Math.min(buffSize, (int) amountToRead));
                if (read == -1) break;
                oldPointer = raf.getFilePointer();
                amountToRead -= read;
                raf.seek(newPointer);
                raf.write(buf);
                newPointer = raf.getFilePointer();
                buf = buf2;
            }
            raf.seek(newPointer);
            raf.write(buf);
            header = newHeader;
            extended_header = newExtHeader;
            is_changed = false;
        } finally {
            try {
                if (raf != null) raf.close();
            } catch (IOException ignored) {
            }
        }
    }
