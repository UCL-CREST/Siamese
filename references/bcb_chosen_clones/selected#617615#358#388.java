    protected void migrateOnDemand() {
        try {
            if (fso.fileExists(prefix + ".fat") && !fso.fileExists(prefix + EXTENSIONS[UBM_FILE])) {
                RandomAccessFile ubm, meta, ctr, rbm;
                InputStream inputStream;
                OutputStream outputStream;
                fso.renameFile(prefix + ".fat", prefix + EXTENSIONS[UBM_FILE]);
                ubm = fso.openFile(prefix + EXTENSIONS[UBM_FILE], "rw");
                meta = fso.openFile(prefix + EXTENSIONS[MTD_FILE], "rw");
                ctr = fso.openFile(prefix + EXTENSIONS[CTR_FILE], "rw");
                ubm.seek(ubm.length() - 16);
                meta.writeInt(blockSize = ubm.readInt());
                meta.writeInt(size = ubm.readInt());
                ctr.setLength(ubm.readLong() + blockSize);
                ctr.close();
                meta.close();
                ubm.setLength(ubm.length() - 16);
                ubm.seek(0);
                rbm = fso.openFile(prefix + EXTENSIONS[UBM_FILE], "rw");
                inputStream = new BufferedInputStream(new RandomAccessFileInputStream(ubm));
                outputStream = new BufferedOutputStream(new RandomAccessFileOutputStream(rbm));
                for (int b; (b = inputStream.read()) != -1; ) outputStream.write(b);
                outputStream.close();
                inputStream.close();
                rbm.close();
                ubm.close();
            }
        } catch (IOException ie) {
            throw new WrappingRuntimeException(ie);
        }
    }
