    private void ZipInfo(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "JOB" + GetId() + ".job";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.println(id);
            writer.println(lang);
            writer.println(name);
            writer.println(cron_exp);
            writer.println(enabled);
            writer.println(userrunas);
            writer.println(creator);
            writer.println(createdate);
        } finally {
            out.closeEntry();
        }
    }
