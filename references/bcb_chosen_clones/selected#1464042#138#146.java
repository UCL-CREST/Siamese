    private void addStream(ZipEntry pzipEntry, InputStream pfileInput) throws Exception {
        mzipOutput.putNextEntry(pzipEntry);
        int llngLength = pfileInput.read(mbfrBytes);
        while (llngLength > 0) {
            mzipOutput.write(mbfrBytes, 0, llngLength);
            llngLength = pfileInput.read(mbfrBytes);
        }
        mzipOutput.closeEntry();
    }
