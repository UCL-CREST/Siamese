    public static byte[] zipEntriesAndFiles(Map<ZipEntry, byte[]> files) throws IOException {
        ByteArrayOutputStream dest = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte[] data = new byte[2048];
        Iterator<Entry<ZipEntry, byte[]>> itr = files.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<ZipEntry, byte[]> entry = itr.next();
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(entry.getValue());
            BufferedInputStream origin = new BufferedInputStream(bytesIn, 2048);
            out.putNextEntry(entry.getKey());
            int count;
            while ((count = origin.read(data, 0, 2048)) != -1) {
                out.write(data, 0, count);
            }
            bytesIn.close();
            origin.close();
        }
        out.close();
        byte[] outBytes = dest.toByteArray();
        dest.close();
        return outBytes;
    }
