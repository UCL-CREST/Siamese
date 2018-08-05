    private void check_file(InputStream in, String cmp) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        assertEquals(out.toString(), cmp);
    }
