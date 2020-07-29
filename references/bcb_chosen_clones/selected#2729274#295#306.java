    public String zipbin(String bin) throws IOException {
        byte[] bytes = bin.getBytes();
        OutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.putNextEntry(new ZipEntry("zip"));
        DataOutputStream os = new DataOutputStream(zos);
        os.write(bytes);
        os.close();
        System.out.println(bos.toString());
        return bos.toString();
    }
