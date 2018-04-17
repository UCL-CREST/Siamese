    static InputStream readEntireFileInBuffer(File file) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(file);
        int n;
        byte buff[] = new byte[1024];
        while ((n = in.read(buff)) > 0) {
            baos.write(buff, 0, n);
        }
        in.close();
        byte data[] = baos.toByteArray();
        baos.close();
        return new ByteArrayInputStream(data);
    }
