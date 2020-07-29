    private void copyZiptoTmpZip(ZipEntry entry, BufferedInputStream in, ZipOutputStream outputFileZip) throws IOException {
        byte[] buf = new byte[1024];
        outputFileZip.putNextEntry(entry);
        int len;
        while ((len = in.read(buf)) > 0) {
            outputFileZip.write(buf, 0, len);
        }
        outputFileZip.closeEntry();
        in.close();
    }
