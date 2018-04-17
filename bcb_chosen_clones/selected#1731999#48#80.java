    static void copyZipEntries(ZipOutputStream zout, File src, Filter filter) throws Throwable {
        ZipFile zipSrc = null;
        try {
            zipSrc = new ZipFile(src);
        } catch (Throwable t) {
            System.out.println("Unexpected error:");
            t.printStackTrace();
            System.out.println();
            System.out.println("Your JDK_DIR cannot handle long JAR entries");
            System.out.println("Please upgrade to JDK 1.4.1 or later.");
            System.out.println("JDK version '1.4.1' is known to work.");
            System.exit(-1);
        }
        for (Enumeration e = zipSrc.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            String name = entry.getName();
            if (name.startsWith("META-INF")) {
                continue;
            }
            if (filter != null && !filter.includeEntry(entry.getName())) {
                continue;
            }
            int size = (int) entry.getSize();
            byte data[] = new byte[size];
            InputStream in = zipSrc.getInputStream(entry);
            DataInputStream din = new DataInputStream(in);
            din.readFully(data);
            zout.putNextEntry(entry);
            zout.write(data, 0, data.length);
            zout.closeEntry();
            din.close();
        }
    }
