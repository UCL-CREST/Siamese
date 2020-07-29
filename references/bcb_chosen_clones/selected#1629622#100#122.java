    public boolean save() {
        try {
            FileOutputStream w = new FileOutputStream(url);
            JarOutputStream jar = new JarOutputStream(w);
            Resource[] values = content.getArray();
            for (int i = 0; i < values.length; i++) {
                ZipEntry ze = new ZipEntry(values[i].getName());
                jar.putNextEntry(ze);
                jar.flush();
                ObjectOutputStream oos = new ObjectOutputStream(jar);
                oos.writeObject(values[i]);
                jar.closeEntry();
                jar.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            DocLog.INSTANCE.log("FileDoc", e);
            return false;
        } catch (IOException e) {
            DocLog.INSTANCE.log("FileDoc", e);
            return false;
        }
    }
