    private static void compressSingleService(ZipOutputStream zout, TranslationServiceAbstract service, String language) throws Exception {
        ByteArrayOutputStream langout = service.compressCache(language);
        if (langout == null) return;
        zout.putNextEntry(new ZipEntry(service.getClass().getCanonicalName()));
        zout.write(langout.toByteArray(), 0, langout.size());
        zout.closeEntry();
    }
