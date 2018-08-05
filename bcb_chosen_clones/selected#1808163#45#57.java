    @Test
    public void testStandardTee() throws Exception {
        final byte[] test = "test".getBytes();
        final InputStream source = new ByteArrayInputStream(test);
        final ByteArrayOutputStream destination1 = new ByteArrayOutputStream();
        final ByteArrayOutputStream destination2 = new ByteArrayOutputStream();
        final TeeOutputStream tee = new TeeOutputStream(destination1, destination2);
        org.apache.commons.io.IOUtils.copy(source, tee);
        tee.close();
        assertArrayEquals("the two arrays are equals", test, destination1.toByteArray());
        assertArrayEquals("the two arrays are equals", test, destination2.toByteArray());
        assertEquals("byte count", test.length, tee.getSize());
    }
