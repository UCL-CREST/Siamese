    protected void addObjectToZip(int serializerType, ZipOutputStream zipOutputStream, Object o, String name, String typeName) throws ValidationException, MarshalException, IOException, InfoException {
        ByteArrayInputStream in = null;
        ObjectOutputStream oos;
        int len;
        byte[] buf = new byte[1024];
        if (serializerType == 1) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(o);
            in = new ByteArrayInputStream(outputStream.toByteArray());
        } else if (serializerType == 2) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = null;
            try {
                String writerClassName;
                String javaVersion = System.getProperty("java.vm.version");
                if (javaVersion.startsWith("1.4")) {
                    writerClassName = "org.apache.xalan.serialize.WriterToUTF8";
                } else {
                    writerClassName = "com.sun.org.apache.xml.internal.serializer.WriterToUTF8";
                }
                Class writerClass = Class.forName(writerClassName);
                writer = (Writer) writerClass.getConstructor(new Class[] { OutputStream.class }).newInstance(new Object[] { outputStream });
            } catch (Exception e) {
                throw new InfoException(LanguageTraslator.traslate("471"), e);
            }
            Marshaller.marshal(o, writer);
            in = new ByteArrayInputStream(outputStream.toByteArray());
        }
        zipOutputStream.putNextEntry(new ZipEntry(name));
        while ((len = in.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, len);
        }
        getConfiguration().put(typeName, name);
        zipOutputStream.closeEntry();
        in.close();
    }
