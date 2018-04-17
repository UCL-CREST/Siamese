    public static boolean replaceFile(String archive, String toReplace, String newReplace) {
        try {
            String tempFile = archive + ".copy";
            com.araya.utilities.FileTextReplacer.copyFile(archive, tempFile);
            byte[] buf = new byte[1024];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archive));
            ZipEntry entry = zin.getNextEntry();
            while (entry != null) {
                String name = entry.getName();
                name = name.replaceAll("\\\\", "/");
                if (toReplace.equals(name)) {
                    ;
                } else {
                    out.putNextEntry(new ZipEntry(name));
                    int len;
                    while ((len = zin.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                entry = zin.getNextEntry();
            }
            zin.close();
            InputStream in = new FileInputStream(newReplace);
            out.putNextEntry(new ZipEntry(toReplace));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            out.close();
            File temp = new File(tempFile);
            temp.delete();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
