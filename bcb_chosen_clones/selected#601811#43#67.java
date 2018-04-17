    public void exportToENARGASTxt(List tecnicos, List revisiones, String postFix) {
        try {
            String fileName = System.getProperty("java.io.tmpdir") + File.separator + "REVTMP.TXT";
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Iterator ir = revisiones.iterator(); ir.hasNext(); ) {
                Revision rev = (Revision) ir.next();
                out.write(this.getENARGASString(rev));
                out.write("\r\n");
            }
            out.close();
            byte[] buf = new byte[1024];
            FileInputStream in = new FileInputStream(fileName);
            ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(path + File.separator + "REV" + postFix + ".zip"));
            zip.putNextEntry(new ZipEntry("REV.TXT"));
            int len;
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
            zip.closeEntry();
            in.close();
            zip.close();
        } catch (IOException e) {
            System.out.println("Error al crear el archivo, exportacion.");
        }
    }
