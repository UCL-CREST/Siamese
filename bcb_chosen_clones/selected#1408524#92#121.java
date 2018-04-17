    public String serializeLexicon() throws Exception {
        URL url = this.getClass().getResource("lex.dic");
        String path = url.getPath();
        String jar_path = path.substring(path.indexOf("/") + 1, path.indexOf("!"));
        String internal_package = path.substring(path.indexOf("!/") + 2, path.lastIndexOf("/"));
        ZipInputStream in = new ZipInputStream(new FileInputStream(new File(jar_path)));
        String new_version = jar_path.substring(0, jar_path.indexOf(".jar")) + ".zip";
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(new_version)));
        ZipEntry entry = null;
        while ((entry = in.getNextEntry()) != null) {
            if (entry.getName().indexOf(ApelonAdditonFileName) != -1) continue;
            out.putNextEntry(entry);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
        if (apelonAdditions != null) {
            out.putNextEntry(new ZipEntry(internal_package + "/" + ApelonAdditonFileName));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bout);
            os.writeObject(apelonAdditions);
            out.write(bout.toByteArray(), 0, bout.size());
            os.close();
        }
        out.closeEntry();
        out.close();
        return new_version;
    }
