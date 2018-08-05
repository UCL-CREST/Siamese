    private void addMatrixToZip(ZipOutputStream zipOutputStream, Matrix matrix, String name, String typeName) throws IOException, InfoException {
        ByteArrayOutputStream out = MatrixCsvSerializer.csvSerialize(matrix);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        int len;
        byte[] buf = new byte[1024];
        zipOutputStream.putNextEntry(new ZipEntry(name));
        while ((len = in.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, len);
        }
        getConfiguration().put(typeName, name);
        zipOutputStream.closeEntry();
        in.close();
    }
