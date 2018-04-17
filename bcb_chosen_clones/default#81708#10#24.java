    public static void main(String[] args) {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("w:\\test.zip", false));
            String name = "";
            for (int i = 0; i < 1000; i++) {
                name += "a";
            }
            ZipEntry ze = new ZipEntry(name);
            zos.putNextEntry(ze);
            zos.write("bbbb".getBytes());
            zos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
