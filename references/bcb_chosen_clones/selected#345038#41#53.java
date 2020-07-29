    @Test
    public void testOther() throws Exception {
        filter.init(this.mockConfig);
        ByteArrayOutputStream jpg = new ByteArrayOutputStream();
        IOUtils.copy(this.getClass().getResourceAsStream("Buffalo-Theory.jpg"), jpg);
        MockFilterChain mockChain = new MockFilterChain();
        mockChain.setContentType("image/jpg");
        mockChain.setOutputData(jpg.toByteArray());
        MockResponse mockResponse = new MockResponse();
        filter.doFilter(this.mockRequest, mockResponse, mockChain);
        Assert.assertTrue("Time stamp content type", "image/jpg".equals(mockResponse.getContentType()));
        Assert.assertTrue("OutputStream as original", ArrayUtils.isEquals(jpg.toByteArray(), mockResponse.getMockServletOutputStream().getBytes()));
    }
