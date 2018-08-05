    private void compressSingleCache(String language, Properties lang, ZipOutputStream zout) throws Exception {
        ByteArrayOutputStream langout = new ByteArrayOutputStream();
        lang.store(langout, "");
        zout.putNextEntry(new ZipEntry(language + ".properties"));
        zout.write(langout.toByteArray(), 0, langout.size());
        zout.closeEntry();
    }
