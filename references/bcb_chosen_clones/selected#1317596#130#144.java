    public static byte[] zipFiles(Map<String, byte[]> files) throws IOException {
        ByteArrayOutputStream dest = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        Iterator<Entry<String, byte[]>> itr = files.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, byte[]> entry = itr.next();
            ZipEntry zipEntry = new ZipEntry(entry.getKey());
            out.putNextEntry(zipEntry);
            IOUtils.write(entry.getValue(), out);
        }
        out.close();
        byte[] outBytes = dest.toByteArray();
        dest.close();
        return outBytes;
    }
