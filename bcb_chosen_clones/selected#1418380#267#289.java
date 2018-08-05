    public static byte[] pack(String filename) throws IOException {
        byte[] data = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.setLevel(0);
        System.out.println("ZippingFile Zipping " + filename);
        try {
            String tmp = (new File(filename)).getName();
            System.out.println("Storing the following file name" + tmp);
            ZipEntry ze = new ZipEntry(tmp);
            zout.putNextEntry(ze);
            FileInputStream fin = new FileInputStream(filename);
            copy(fin, zout);
            zout.closeEntry();
            fin.close();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FileNotProcessed Problems processing " + filename);
        }
        zout.close();
        data = out.toByteArray();
        out.close();
        return data;
    }
