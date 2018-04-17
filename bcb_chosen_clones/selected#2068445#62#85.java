    public void createPdf(String filename) throws SQLException, IOException, DocumentException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4, 36, 36, 72, 36);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        useStationary(writer);
        document.open();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT country, id FROM film_country ORDER BY country");
        while (rs.next()) {
            document.add(new Paragraph(rs.getString("country"), FilmFonts.BOLD));
            document.add(Chunk.NEWLINE);
            Set<Movie> movies = new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getString("id")));
            for (Movie movie : movies) {
                document.add(new Paragraph(movie.getMovieTitle(), FilmFonts.BOLD));
                if (movie.getOriginalTitle() != null) document.add(new Paragraph(movie.getOriginalTitle(), FilmFonts.ITALIC));
                document.add(new Paragraph(String.format("Year: %d; run length: %d minutes", movie.getYear(), movie.getDuration()), FilmFonts.NORMAL));
                document.add(PojoToElementFactory.getDirectorList(movie));
            }
            document.newPage();
        }
        document.close();
        connection.close();
    }
