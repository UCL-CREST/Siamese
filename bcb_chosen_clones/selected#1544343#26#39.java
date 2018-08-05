    @Test
    public void testCopy() throws IOException {
        final byte[] input = { 0x00, 0x01, 0x7F, 0x03, 0x40 };
        final byte[] verification = input.clone();
        Assert.assertNotSame("Expecting verification to be a new array.", input, verification);
        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        final byte[] output = out.toByteArray();
        Assert.assertTrue("Expecting input to be unchanged.", Arrays.equals(verification, input));
        Assert.assertTrue("Expecting output to be like input.", Arrays.equals(verification, output));
        Assert.assertNotSame("Expecting output to be a new array.", input, output);
        Assert.assertNotSame("Expecting output to be a new array.", verification, output);
    }
