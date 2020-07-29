    @Test
    public void testStandardTee() throws Exception {
        final String reference = "test";
        final Reader source = new StringReader(reference);
        final StringWriter destination1 = new StringWriter();
        final StringWriter destination2 = new StringWriter();
        final TeeWriter tee = new TeeWriter(destination1, destination2);
        org.apache.commons.io.IOUtils.copy(source, tee);
        tee.close();
        assertEquals("the two string are equals", reference, destination1.toString());
        assertEquals("the two string are equals", reference, destination2.toString());
        assertEquals("byte count", reference.length(), tee.getSize());
    }
