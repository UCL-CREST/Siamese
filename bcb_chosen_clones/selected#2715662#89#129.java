    private static void createTestFile(File file, String encoding, boolean withEFS, boolean withExplicitUnicodeExtra) throws UnsupportedEncodingException, IOException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(file);
            zos.setEncoding(encoding);
            zos.setUseLanguageEncodingFlag(withEFS);
            zos.setCreateUnicodeExtraFields(withExplicitUnicodeExtra ? ZipOutputStream.UnicodeExtraFieldPolicy.NEVER : ZipOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
            ZipEntry ze = new ZipEntry(OIL_BARREL_TXT);
            if (withExplicitUnicodeExtra && !zipEncoding.canEncode(ze.getName())) {
                ByteBuffer en = zipEncoding.encode(ze.getName());
                ze.addExtraField(new UnicodePathExtraField(ze.getName(), en.array(), en.arrayOffset(), en.limit()));
            }
            zos.putNextEntry(ze);
            zos.write("Hello, world!".getBytes("US-ASCII"));
            zos.closeEntry();
            ze = new ZipEntry(EURO_FOR_DOLLAR_TXT);
            if (withExplicitUnicodeExtra && !zipEncoding.canEncode(ze.getName())) {
                ByteBuffer en = zipEncoding.encode(ze.getName());
                ze.addExtraField(new UnicodePathExtraField(ze.getName(), en.array(), en.arrayOffset(), en.limit()));
            }
            zos.putNextEntry(ze);
            zos.write("Give me your money!".getBytes("US-ASCII"));
            zos.closeEntry();
            ze = new ZipEntry(ASCII_TXT);
            if (withExplicitUnicodeExtra && !zipEncoding.canEncode(ze.getName())) {
                ByteBuffer en = zipEncoding.encode(ze.getName());
                ze.addExtraField(new UnicodePathExtraField(ze.getName(), en.array(), en.arrayOffset(), en.limit()));
            }
            zos.putNextEntry(ze);
            zos.write("ascii".getBytes("US-ASCII"));
            zos.closeEntry();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                }
            }
        }
    }
