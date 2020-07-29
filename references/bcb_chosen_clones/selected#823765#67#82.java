    public static void zip(InputStream in, OutputStream out, String zipEntryName) throws IOException {
        ZipOutputStream zOut = new ZipOutputStream(out);
        zOut.setLevel(9);
        zOut.setMethod(Deflater.DEFLATED);
        ZipEntry zEntry = new ZipEntry(zipEntryName);
        zOut.putNextEntry(zEntry);
        byte[] data = new byte[10000];
        int inByte = in.read(data);
        while (inByte != -1) {
            zOut.write(data, 0, inByte);
            inByte = in.read(data);
        }
        zOut.closeEntry();
        zOut.close();
        in.close();
    }
