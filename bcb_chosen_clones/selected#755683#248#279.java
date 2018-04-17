    private void transformFile(File input, File output, Cipher cipher, boolean compress, String progressMessage) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(input);
        InputStream inputStream;
        if (progressMessage != null) {
            inputStream = new ProgressMonitorInputStream(null, progressMessage, fileInputStream);
        } else {
            inputStream = fileInputStream;
        }
        FilterInputStream is = new BufferedInputStream(inputStream);
        FilterOutputStream os = new BufferedOutputStream(new FileOutputStream(output));
        FilterInputStream fis;
        FilterOutputStream fos;
        if (compress) {
            fis = is;
            fos = new GZIPOutputStream(new CipherOutputStream(os, cipher));
        } else {
            fis = new GZIPInputStream(new CipherInputStream(is, cipher));
            fos = os;
        }
        byte[] buffer = new byte[cipher.getBlockSize() * blocksInBuffer];
        int readLength = fis.read(buffer);
        while (readLength != -1) {
            fos.write(buffer, 0, readLength);
            readLength = fis.read(buffer);
        }
        if (compress) {
            GZIPOutputStream gos = (GZIPOutputStream) fos;
            gos.finish();
        }
        fos.close();
        fis.close();
    }
