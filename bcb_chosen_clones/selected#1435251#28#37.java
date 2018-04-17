    public void write(ZIClassInfo classInfo) throws Exception {
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        ZipEntry ze = new ZipEntry("classinfo.ser");
        zip.putNextEntry(ze);
        ObjectOutputStream os = new ObjectOutputStream(zip);
        os.writeObject(classInfo);
        os.flush();
        zip.closeEntry();
        zip.close();
    }
