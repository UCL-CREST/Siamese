    public void serialize(String name) throws FileNotFoundException, IOException {
        String path = name + ".jar";
        FileOutputStream fos = new FileOutputStream(path);
        JarOutputStream jos = new JarOutputStream(fos);
        ZipEntry ze = new ZipEntry(name);
        jos.putNextEntry(ze);
        ObjectOutputStream oos = new ObjectOutputStream(jos);
        oos.writeObject(this);
        oos.close();
    }
