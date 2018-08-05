    @Override
    public void write(DataOutput dataOutput, byte[] object) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new DataOutputOutputStream(dataOutput));
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.putNextEntry(new ZipEntry("a"));
        zos.write(object, 0, object.length);
        zos.finish();
        zos.close();
    }
