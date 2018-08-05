    @Test
    public void testLoadSource() throws IOException {
        ArticleMetadata metadata = new ArticleMetadata();
        metadata.setId("http://arxiv.org/abs/math/0205003v1");
        InputStream inputStream = arxivDAOFacade.loadSource(metadata);
        Assert.assertNotNull(inputStream);
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "utf8");
        String contents = writer.toString();
        Assert.assertTrue(contents.contains("A strengthening of the Nyman"));
        inputStream.close();
    }
