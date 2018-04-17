    public static byte[] zipObject(Object o) {
        if (o == null) return null;
        byte[] returnValue = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
            byte[] binaryData = baos.toByteArray();
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos2);
            ZipEntry ze = new ZipEntry("compressed_obj");
            zos.putNextEntry(ze);
            zos.write(binaryData);
            zos.closeEntry();
            zos.flush();
            zos.close();
            returnValue = baos2.toByteArray();
            baos.close();
            baos2.close();
        } catch (IOException ex) {
            returnValue = null;
        }
        return returnValue;
    }
