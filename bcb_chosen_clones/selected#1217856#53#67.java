    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT name, given_name FROM film_director ORDER BY name, given_name");
        while (rs.next()) {
            document.add(createDirectorPhrase(rs));
            document.add(Chunk.NEWLINE);
        }
        stm.close();
        connection.close();
        document.close();
    }
