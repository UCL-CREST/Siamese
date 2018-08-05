    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename)).setInitialLeading(16);
        document.open();
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT country, id FROM film_country ORDER BY country");
        Font font = new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
        while (rs.next()) {
            document.add(new Chunk(rs.getString("country")));
            document.add(new Chunk(" "));
            Chunk id = new Chunk(rs.getString("id"), font);
            id.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
            id.setTextRise(6);
            document.add(id);
            document.add(Chunk.NEWLINE);
        }
        stm.close();
        connection.close();
        document.close();
    }
