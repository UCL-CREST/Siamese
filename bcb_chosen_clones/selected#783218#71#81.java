    @Override
    public void write(final DataOutput dataOutput, T object) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new DataOutputOutputStream(dataOutput));
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.putNextEntry(new ZipEntry("a"));
        DataOutputStream dos = new DataOutputStream(zos);
        converter.write(dos, object);
        dos.flush();
        zos.finish();
        dos.close();
    }
