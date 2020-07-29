    private static void writeFile(String fullname, String path) throws FileNotFoundException {
        byte buffer[] = new byte[1024];
        FileInputStream input = new FileInputStream(new File(path + fullname));
        try {
            out.putNextEntry(new ZipEntry(fullname));
            int len;
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
