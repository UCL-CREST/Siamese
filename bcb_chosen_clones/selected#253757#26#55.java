    public static boolean unZip(String zipname, String extractTo) {
        InputStream is;
        ZipInputStream zis;
        try {
            is = new FileInputStream(extractTo + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                String filename = ze.getName();
                FileOutputStream fout = new FileOutputStream(extractTo + filename);
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                    byte[] bytes = baos.toByteArray();
                    fout.write(bytes);
                    baos.reset();
                }
                fout.flush();
                fout.close();
                zis.closeEntry();
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
