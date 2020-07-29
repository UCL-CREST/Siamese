    private void addVerifier(ZipOutputStream zos, File verifierBinary) throws IOException, ClientBinaryNotFoundException {
        InputStream in = new FileInputStream(verifierBinary);
        if (in == null) {
            throw new ClientBinaryNotFoundException();
        }
        ZipEntry entry = new ZipEntry("verifiers/" + verifierBinary.getName());
        zos.putNextEntry(entry);
        byte[] buf = new byte[1024];
        int data;
        while ((data = in.read(buf)) > -1) {
            zos.write(buf, 0, data);
        }
        zos.closeEntry();
        in.close();
    }
