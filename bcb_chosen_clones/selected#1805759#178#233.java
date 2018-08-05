    private void writeEntry(JarOutputStream j, String name, long mtime, long lsize, boolean deflateHint, ByteBuffer data0, ByteBuffer data1) throws IOException {
        int size = (int) lsize;
        if (size != lsize) throw new IOException("file too large: " + lsize);
        CRC32 crc32 = _crc32;
        if (_verbose > 1) Utils.log.fine("Writing entry: " + name + " size=" + size + (deflateHint ? " deflated" : ""));
        if (_buf.length < size) {
            int newSize = size;
            while (newSize < _buf.length) {
                newSize <<= 1;
                if (newSize <= 0) {
                    newSize = size;
                    break;
                }
            }
            _buf = new byte[newSize];
        }
        assert (_buf.length >= size);
        int fillp = 0;
        if (data0 != null) {
            int size0 = data0.capacity();
            data0.get(_buf, fillp, size0);
            fillp += size0;
        }
        if (data1 != null) {
            int size1 = data1.capacity();
            data1.get(_buf, fillp, size1);
            fillp += size1;
        }
        while (fillp < size) {
            int nr = in.read(_buf, fillp, size - fillp);
            if (nr <= 0) throw new IOException("EOF at end of archive");
            fillp += nr;
        }
        ZipEntry z = new ZipEntry(name);
        z.setTime((long) mtime * 1000);
        if (size == 0) {
            z.setMethod(ZipOutputStream.STORED);
            z.setSize(0);
            z.setCrc(0);
            z.setCompressedSize(0);
        } else if (!deflateHint) {
            z.setMethod(ZipOutputStream.STORED);
            z.setSize(size);
            z.setCompressedSize(size);
            crc32.reset();
            crc32.update(_buf, 0, size);
            z.setCrc(crc32.getValue());
        } else {
            z.setMethod(Deflater.DEFLATED);
            z.setSize(size);
        }
        j.putNextEntry(z);
        if (size > 0) j.write(_buf, 0, size);
        j.closeEntry();
        if (_verbose > 0) Utils.log.info("Writing " + Utils.zeString(z));
    }
