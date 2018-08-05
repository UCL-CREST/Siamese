    private void testAll() throws Exception {
        final byte laData[] = new byte[ZipTesting.DATA_BUFFER];
        final CRC32 loCRC32 = new CRC32();
        Exception loException = null;
        try {
            final ZipFile loZipFile = new ZipFile(this.fcZipFile);
            ZipEntry loEntry;
            final Enumeration<? extends ZipEntry> loEnum = loZipFile.entries();
            final int lnCount = loZipFile.size();
            for (int i = 0; i < lnCount; ++i) {
                loEntry = loEnum.nextElement();
                if (loEntry.isDirectory()) {
                    continue;
                }
                loCRC32.reset();
                final BufferedInputStream loInput = new BufferedInputStream(loZipFile.getInputStream(loEntry));
                final long lnFileSize = loEntry.getSize();
                long lnTotalRead = 0;
                int lnBytesRead;
                lnBytesRead = loInput.read(laData, 0, ZipTesting.DATA_BUFFER);
                while (lnBytesRead != -1) {
                    loCRC32.update(laData, 0, lnBytesRead);
                    lnTotalRead += lnBytesRead;
                    lnBytesRead = loInput.read(laData, 0, ZipTesting.DATA_BUFFER);
                }
                if (loEntry.getCrc() != loCRC32.getValue()) {
                    throw (new Exception("Check sums are corrupt for " + loEntry.getName() + " in the zip file, " + this.fcZipFile + "."));
                }
                if (lnTotalRead != lnFileSize) {
                    throw (new Exception("Read " + lnTotalRead + " bytes for " + loEntry.getName() + " in the zip file, " + this.fcZipFile + ": should be " + lnFileSize + " bytes."));
                }
            }
        } catch (final Exception loErr) {
            loException = loErr;
        }
        if (loException != null) {
            throw (loException);
        }
    }
