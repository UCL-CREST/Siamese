    @Test
    public void testExactCopySize() throws IOException {
        final int size = Byte.SIZE + RANDOMIZER.nextInt(TEST_DATA.length - Long.SIZE);
        final InputStream in = new ByteArrayInputStream(TEST_DATA);
        final ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        final int cpySize = ExtraIOUtils.copy(in, out, size);
        assertEquals("Mismatched copy size", size, cpySize);
        final byte[] subArray = ArrayUtils.subarray(TEST_DATA, 0, size), outArray = out.toByteArray();
        assertArrayEquals("Mismatched data", subArray, outArray);
    }
