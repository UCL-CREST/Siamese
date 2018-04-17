    public static void addFileToJar(String jarandResource, String f) {
        String[] tmp = extractJarAndResourceName(jarandResource);
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tmp[0]));
            ZipInputStream fin = new ZipInputStream(new FileInputStream(f));
            out.putNextEntry(new ZipEntry(tmp[1]));
            byte[] buf = new byte[fin.available()];
            fin.read(buf);
            out.write(buf);
            out.flush();
            out.closeEntry();
            out.close();
        } catch (Exception e) {
            Logger.logErr("addFileToJar: Jar: " + tmp[0] + " File: " + tmp[1] + " : " + e.toString());
        }
    }
