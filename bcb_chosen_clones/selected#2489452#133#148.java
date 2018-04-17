    public static byte[] zipByteArray(byte[] input, String zipEntryName) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
            ZipOutputStream out = new ZipOutputStream(bos);
            out.putNextEntry(new ZipEntry(zipEntryName));
            out.write(input, 0, input.length);
            out.closeEntry();
            out.finish();
            out.close();
            byte[] compressedData = bos.toByteArray();
            return compressedData;
        } catch (IOException e) {
            log.error("Caught exception zipping byte array: " + e, e);
            return null;
        }
    }
