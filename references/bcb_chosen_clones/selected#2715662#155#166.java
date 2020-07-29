    private static void assertUnicodeName(ZipEntry ze, String expectedName, String encoding) throws IOException {
        if (!expectedName.equals(ze.getName())) {
            UnicodePathExtraField ucpf = findUniCodePath(ze);
            assertNotNull(ucpf);
            ZipEncoding enc = ZipEncodingHelper.getZipEncoding(encoding);
            ByteBuffer ne = enc.encode(ze.getName());
            CRC32 crc = new CRC32();
            crc.update(ne.array(), ne.arrayOffset(), ne.limit());
            assertEquals(crc.getValue(), ucpf.getNameCRC32());
            assertEquals(expectedName, new String(ucpf.getUnicodeName(), UTF_8));
        }
    }
