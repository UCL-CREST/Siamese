    public static void zipFiles(File src, File out, FileFilters pattern, boolean verbos) throws Exception {
        src = src.getCanonicalFile();
        out = out.getCanonicalFile();
        if (!src.isHidden()) {
            String root = "";
            if (src.isDirectory()) {
                root = src.getCanonicalPath();
            } else if (src.isFile()) {
                root = src.getParentFile().getCanonicalPath();
            }
            LinkedHashMap<String, byte[]> entrys = new LinkedHashMap<String, byte[]>();
            zipFiles(src, pattern, root, entrys, verbos);
            if (!entrys.isEmpty()) {
                out.createNewFile();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos);
                for (Entry<String, byte[]> e : entrys.entrySet()) {
                    ZipEntry ze = new ZipEntry(e.getKey());
                    ze.setTime(0);
                    zos.putNextEntry(ze);
                    zos.write(e.getValue());
                }
                zos.close();
                CFile.writeData(out, baos.toByteArray());
            }
        }
    }
