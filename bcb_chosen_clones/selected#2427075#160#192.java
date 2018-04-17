    static List<String> listProperties(final MetadataType type) {
        List<String> props = new ArrayList<String>();
        try {
            File adapter = File.createTempFile("adapter", null);
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(type.adapter);
            if (stream == null) {
                throw new IllegalStateException("Could not load adapter Jar: " + type.adapter);
            }
            FileOutputStream out = new FileOutputStream(adapter);
            IOUtils.copyLarge(stream, out);
            out.close();
            JarFile jar = new JarFile(adapter);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith("dtd")) {
                    InputStream inputStream = jar.getInputStream(entry);
                    Scanner s = new Scanner(inputStream);
                    while (s.hasNextLine()) {
                        String nextLine = s.nextLine();
                        if (nextLine.startsWith("<!ELEMENT")) {
                            String prop = nextLine.split(" ")[1];
                            props.add(prop);
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
