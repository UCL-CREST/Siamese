    private static void aggFile(ZipOutputStream out, String strPathOrigine, String nomefile) throws Exception {
        String separatore = System.getProperty("file.separator");
        try {
            FileInputStream fi = new FileInputStream(strPathOrigine + separatore + nomefile);
            BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(nomefile);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            ;
            origin.close();
            origin = null;
            fi.close();
            fi = null;
        } catch (Exception e) {
            new Exception("\n aggFile: " + e.getMessage() + "\n");
        }
    }
