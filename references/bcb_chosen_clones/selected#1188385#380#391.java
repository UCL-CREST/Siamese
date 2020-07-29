    private void storeTagInZip(String zipFileName, ZipOutputStream zipOut) throws IOException {
        zipOut.putNextEntry(new ZipEntry(TAG_FILENAME));
        String oldTag = null;
        if (zipTags != null) oldTag = (String) zipTags.get(zipFileName);
        Writer out = new OutputStreamWriter(zipOut);
        if (oldTag != null) out.write(oldTag); else out.write("# This file facilitates revision tracking.\n");
        String newTag = System.currentTimeMillis() + "-" + Math.abs(random.nextLong());
        out.write(newTag);
        out.write('\n');
        out.flush();
        zipOut.closeEntry();
    }
