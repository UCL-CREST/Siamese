    private static void zipDirectory(File directory, String name, ZipOutputStream zos) throws IOException {
        name += "/";
        zos.putNextEntry(new ZipEntry(name));
        zos.closeEntry();
        String[] entryList = directory.list();
        for (int i = 0; i < entryList.length; ++i) {
            File f = new File(directory, entryList[i]);
            if (f.isDirectory()) {
                zipDirectory(f, name + f.getName(), zos);
            } else {
                FileInputStream fis = new FileInputStream(f);
                ZipEntry entry = new ZipEntry(name + f.getName());
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesIn = 0;
                zos.putNextEntry(entry);
                while ((bytesIn = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesIn);
                }
                fis.close();
                zos.closeEntry();
            }
        }
    }
