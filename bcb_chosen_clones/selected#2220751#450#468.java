    protected void ZipTemplate(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename_info = GetClassName() + ".data";
        out.putNextEntry(new ZipEntry(filename_info));
        Reader r = null;
        try {
            r = GetTemplate(ctxt);
            int b;
            while ((b = r.read()) != -1) {
                out.write(b);
            }
        } finally {
            if (r != null) try {
                r.close();
            } catch (Exception e1) {
            }
            Clear();
            out.closeEntry();
        }
    }
