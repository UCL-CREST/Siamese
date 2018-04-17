    public static byte[] zipEntriesAndFiles(Map<ZipEntry, byte[]> files) throws Exception {
        ByteArrayOutputStream dest = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte[] data = new byte[2048];
        Iterator<ZipEntry> itr = files.keySet().iterator();
        while (itr.hasNext()) {
            ZipEntry entry = itr.next();
            byte[] tempFile = files.get(entry);
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(tempFile);
            BufferedInputStream origin = new BufferedInputStream(bytesIn, 2048);
            out.putNextEntry(entry);
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
