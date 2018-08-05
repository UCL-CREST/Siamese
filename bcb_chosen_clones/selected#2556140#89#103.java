    public static void zipTest() {
        try {
            FileOutputStream fos = new FileOutputStream("/home/test.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry("a1.txt"));
            zos.write("just a test".getBytes());
            zos.write("\r\nand u are here~".getBytes());
            zos.putNextEntry(new ZipEntry("cc.txt"));
            zos.write("another file~".getBytes());
            zos.setComment("i can zip it~");
            zos.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
