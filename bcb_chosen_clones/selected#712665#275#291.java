    public static byte[] getBytes(final Serializable o, final boolean zip) throws IOException {
        if (o == null) {
            return IOUtils.NBYTE;
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        if (zip) {
            final ByteArrayOutputStream ret = new ByteArrayOutputStream();
            final ZipOutputStream zos = new ZipOutputStream(ret);
            zos.putNextEntry(new ZipEntry("obj"));
            zos.write(bos.toByteArray());
            zos.close();
            return ret.toByteArray();
        }
        return bos.toByteArray();
    }
