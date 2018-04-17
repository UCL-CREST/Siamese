    public void createPdf(String filename, int viewerpreference) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        writer.setViewerPreferences(viewerpreference);
        document.open();
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            Paragraph p = createMovieInformation(movie);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.setIndentationLeft(18);
            p.setFirstLineIndent(-18);
            document.add(p);
        }
        document.close();
        connection.close();
    }
