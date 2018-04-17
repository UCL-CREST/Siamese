    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfContentByte over = writer.getDirectContent();
        PdfContentByte under = writer.getDirectContentUnder();
        try {
            DatabaseConnection connection = new HsqldbConnection("filmfestival");
            locations = PojoFactory.getLocations(connection);
            List<Date> days = PojoFactory.getDays(connection);
            List<Screening> screenings;
            for (Date day : days) {
                drawTimeTable(under);
                drawTimeSlots(over);
                screenings = PojoFactory.getScreenings(connection, day);
                for (Screening screening : screenings) {
                    drawBlock(screening, under, over);
                }
                document.newPage();
            }
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            document.add(new Paragraph("Database error: " + sqle.getMessage()));
        }
        document.close();
    }
