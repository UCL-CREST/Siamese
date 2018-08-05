    private static void execute(String fileName) throws IOException, SQLException {
        InputStream input = DatabaseConstants.class.getResourceAsStream(fileName);
        StringWriter writer = new StringWriter();
        IOUtils.copy(input, writer);
        String sql = writer.toString();
        Statement statement = connection.createStatement();
        statement.execute(sql);
    }
