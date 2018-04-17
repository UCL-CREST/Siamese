    public static byte[] getBinaryFileContent(String fileName) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName));
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(bs);
        byte[] ioBuf = new byte[4096];
        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(ioBuf)) != -1) {
            out.write(ioBuf, 0, bytesRead);
        }
        out.close();
        bufferedInputStream.close();
        return bs.toByteArray();
    }
