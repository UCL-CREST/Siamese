    public static void createResource(String name, Object o, File out) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(out));
        try {
            zip.putNextEntry(new ZipEntry(PREFIX + o.getClass().getName()));
            zip.write(name.getBytes("utf-8"));
            zip.putNextEntry(new ZipEntry(name));
            ObjectOutputStream oos = new ObjectOutputStream(zip);
            oos.writeObject(o);
            oos.close();
        } finally {
            zip.close();
        }
    }
