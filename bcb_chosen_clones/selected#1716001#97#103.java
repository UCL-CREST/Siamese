    static void addResourceInputEntry(ZipOutputStream zout) throws Throwable {
        ZipEntry entry = new ZipEntry("javaapi/com/sun/cldc/io/sampledata.txt");
        byte data[] = new byte[100];
        zout.putNextEntry(entry);
        zout.write(data, 0, data.length);
        zout.closeEntry();
    }
