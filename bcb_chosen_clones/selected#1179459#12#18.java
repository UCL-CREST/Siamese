    @Test
    @Ignore
    public void testToJson() throws IOException {
        JsonSerializer js = new StreamingJsonSerializer(new ObjectMapper());
        BulkOperation op = js.createBulkOperation(createTestData(10000), false);
        IOUtils.copy(op.getData(), System.out);
    }
