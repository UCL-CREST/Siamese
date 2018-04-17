    public void execute(Object instance) throws IOException, XmlPersistException {
        System.out.println();
        System.out.println();
        System.out.println(String.format("persist %s using %s", description, persisterDescription));
        String filename1 = String.format(".tmp/%s.%s.xml", description, persisterDescription);
        FileOutputStream out = new FileOutputStream(filename1);
        try {
            xmlPersister.save(xmlBindingModel, instance, out);
            xmlPersister.save(xmlBindingModel, instance, System.out);
        } finally {
            out.flush();
            out.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("read %s using %s", description, persisterDescription));
        Object readInstance;
        FileInputStream in = new FileInputStream(filename1);
        try {
            readInstance = xmlPersister.load(xmlBindingModel, persistedClass, in);
        } finally {
            in.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("rewrite %s using %s", description, persisterDescription));
        String filename2 = String.format(".tmp/%s.%s.2.xml", description, persisterDescription);
        out = new FileOutputStream(filename2);
        try {
            xmlPersister.save(xmlBindingModel, readInstance, out);
            xmlPersister.save(xmlBindingModel, readInstance, System.out);
        } finally {
            out.flush();
            out.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("serielize %s to byte[]", description));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        xmlPersister.save(xmlBindingModel, instance, byteArrayOutputStream);
        byte[] serializedObject = byteArrayOutputStream.toByteArray();
        System.out.println(serializedObject);
        System.out.println();
        System.out.println();
        System.out.println(String.format("deserielize %s from byte[]", description));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
        readInstance = xmlPersister.load(xmlBindingModel, persistedClass, byteArrayInputStream);
        xmlPersister.save(xmlBindingModel, readInstance, System.out);
        BufferedReader readerOnFile1 = new BufferedReader(new FileReader(filename1));
        BufferedReader readerOnFile2 = new BufferedReader(new FileReader(filename2));
        try {
            StringBuilder file1 = new StringBuilder();
            String line;
            while ((line = readerOnFile1.readLine()) != null) {
                file1.append(line).append("\n");
            }
            StringBuilder file2 = new StringBuilder();
            while ((line = readerOnFile2.readLine()) != null) {
                file2.append(line).append("\n");
            }
            assertEquals(file1.toString(), file2.toString());
        } finally {
            readerOnFile1.close();
            readerOnFile2.close();
        }
    }
