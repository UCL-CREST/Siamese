    private static byte[] loadFile(File f) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(f);
        int i;
        byte b[] = new byte[1024 * 1024];
        while ((i = in.read(b)) != -1) baos.write(b, 0, i);
        in.close();
        baos.close();
        return baos.toByteArray();
    }
