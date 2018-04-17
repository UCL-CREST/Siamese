    @Test
    public void testCopyUnknownSize() throws IOException {
        final InputStream in = new ByteArrayInputStream(TEST_DATA);
        final ByteArrayOutputStream out = new ByteArrayOutputStream(TEST_DATA.length);
        final int cpySize = ExtraIOUtils.copy(in, out, (-1));
        assertEquals("Mismatched copy size", TEST_DATA.length, cpySize);
        final byte[] outArray = out.toByteArray();
        assertArrayEquals("Mismatched data", TEST_DATA, outArray);
    }
