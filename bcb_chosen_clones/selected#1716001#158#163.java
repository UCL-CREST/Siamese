    static void writeToZip(ZipOutputStream zout, ByteArrayOutputStream baos, String name) throws Throwable {
        ZipEntry entry = new ZipEntry("javaapi/com/sun/cldc/io/" + name);
        zout.putNextEntry(entry);
        baos.writeTo(zout);
        zout.closeEntry();
    }
