    public Block compress() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.putNextEntry(new ZipEntry("default"));
            zos.write(array, offset, size);
            zos.finish();
            zos.close();
            return new Block(baos.toByteArray());
        } catch (IOException e) {
            throw new WrappingRuntimeException(e);
        }
    }
