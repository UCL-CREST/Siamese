    private void ZipCode(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "JOB" + GetId() + ".data";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.print(code);
        } finally {
            out.closeEntry();
        }
    }
