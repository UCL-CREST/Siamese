    File createJar(String name, String... entries) throws IOException {
        File jar = new File(name);
        OutputStream out = new FileOutputStream(jar);
        try {
            JarOutputStream jos = new JarOutputStream(out);
            for (String e : entries) {
                jos.putNextEntry(new ZipEntry(e));
                jos.write(e.getBytes());
            }
            jos.close();
        } finally {
            out.close();
        }
        return jar;
    }
