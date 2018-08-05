    static void addReadCompletely1Entry(ZipOutputStream zout) throws Throwable {
        String name = "vm/share/runtime/ClassPathAccess/read_completely1.dat";
        System.out.println("adding \"" + name + "\"");
        ZipEntry entry = new ZipEntry(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 256; j++) {
                baos.write(j);
            }
            int count = 10240;
            for (int k = 0; k < count; k++) {
                baos.write((local_rand() >> 24) & 0xff);
            }
        }
        byte data[] = baos.toByteArray();
        zout.putNextEntry(entry);
        zout.write(data, 0, data.length);
        zout.closeEntry();
    }
