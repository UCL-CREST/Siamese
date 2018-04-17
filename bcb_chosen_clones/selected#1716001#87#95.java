    static void addTestEntry(ZipOutputStream zout, int numLevels, String name) throws Throwable {
        System.out.println("adding in very long path: \"" + name + "\"");
        String longName = getLongPath(numLevels, "/") + name;
        ZipEntry entry = new ZipEntry(longName);
        byte data[] = new byte[100];
        zout.putNextEntry(entry);
        zout.write(data, 0, data.length);
        zout.closeEntry();
    }
