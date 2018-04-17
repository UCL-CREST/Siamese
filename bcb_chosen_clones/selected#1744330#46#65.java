    public static void writeFile(String fname, String entry) {
        byte[] buf = new byte[1024];
        try {
            FileInputStream in = new FileInputStream(entry);
            if (fname == null) {
                out.putNextEntry(new ZipEntry(entry));
            } else {
                out.putNextEntry(new ZipEntry(fname));
            }
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            out.closeEntry();
            in.close();
        } catch (Exception e) {
            System.out.println("Error while writing to ZIP Stream!" + e);
        }
    }
