    public void write(OutputStream out, String className, InputStream classDefStream) throws IOException {
        ByteArrayOutputStream a = new ByteArrayOutputStream();
        IOUtils.copy(classDefStream, a);
        a.close();
        DataOutputStream da = new DataOutputStream(out);
        da.writeUTF(className);
        da.writeUTF(new String(base64.cipher(a.toByteArray())));
    }
