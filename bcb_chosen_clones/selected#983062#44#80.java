    public static final byte[] compress(String str) {
        if (str == null) return null;
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            ZipEntry zipentry = new ZipEntry("0.txt");
            zipentry.setMethod(ZipEntry.STORED);
            zipentry.setSize(str.getBytes().length);
            CRC32 crc32 = new CRC32();
            crc32.update(str.getBytes(), 0, str.getBytes().length);
            zipentry.setCrc(crc32.getValue());
            zout.putNextEntry(zipentry);
            zout.write(str.getBytes());
            zout.closeEntry();
            compressed = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            compressed = null;
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return compressed;
    }
