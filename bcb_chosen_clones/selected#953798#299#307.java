    public static void zi(String s, String z, int i) throws Exception {
        byte[] buffer = s.getBytes("UTF-8");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(z + ".zip"));
        out.putNextEntry(new ZipEntry(i + ".txt"));
        out.write(buffer);
        out.closeEntry();
        out.close();
        System.out.println(i + ".txt -> " + z + ".zip");
    }
