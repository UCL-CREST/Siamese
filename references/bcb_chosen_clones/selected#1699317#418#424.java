    private void checkInputStream(InputStream in, byte[] cmp, boolean all) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        IOUtils.copy(in, stream);
        byte[] out = stream.toByteArray();
        if (all) assertEquals(cmp.length, out.length);
        for (int i = 0; i < cmp.length; i++) assertEquals(cmp[i], out[i]);
    }
