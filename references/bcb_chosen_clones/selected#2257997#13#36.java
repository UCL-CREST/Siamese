    public static void main(String args[]) {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream joe = new DataOutputStream(stream);
        try {
            joe.writeBytes("PERRACHICA\n");
            joe.writeUTF("PEROQUESETO\n");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        int len;
        try {
            String outFile = "c:\\dns.zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFile));
            out.putNextEntry(new ZipEntry("feo\\chu.txt"));
            ByteArrayInputStream entrada = new ByteArrayInputStream(stream.toByteArray());
            while ((len = entrada.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
