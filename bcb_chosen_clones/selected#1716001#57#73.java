    static void copyEntries(ZipOutputStream zout, InputStream in) throws Throwable {
        ZipInputStream zin = new ZipInputStream(in);
        for (; ; ) {
            ZipEntry entry = zin.getNextEntry();
            if (entry == null) {
                break;
            }
            int size = (int) entry.getSize();
            byte data[] = new byte[size];
            zin.read(data, 0, size);
            zin.closeEntry();
            zout.putNextEntry(entry);
            zout.write(data, 0, data.length);
            zout.closeEntry();
        }
        zin.close();
    }
