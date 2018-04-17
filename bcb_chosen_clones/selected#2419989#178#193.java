    @Override
    public InputStream getInputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(0);
        ZipOutputStream jos = new ZipOutputStream(os);
        String className = "Mock";
        ClassBuilder b = new ClassBuilder(className);
        byte[] classData = b.finish();
        ZipEntry e = new ZipEntry(className + ".class");
        jos.putNextEntry(e);
        jos.write(classData);
        jos.closeEntry();
        jos.finish();
        jos.close();
        byte[] data = os.toByteArray();
        return new ByteArrayInputStream(data);
    }
