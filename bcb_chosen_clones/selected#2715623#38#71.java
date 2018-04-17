    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c " + "FROM film_director d, film_movie_director md WHERE d.id = md.director_id " + "GROUP BY d.id, d.name, d.given_name ORDER BY c DESC");
        Director director;
        while (rs.next()) {
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph(PojoToElementFactory.getDirectorPhrase(director));
            p.add(new Chunk(new DottedLineSeparator()));
            p.add(String.format("movies: %d", rs.getInt("c")));
            document.add(p);
            List list = new List(List.ORDERED);
            list.setIndentationLeft(36);
            list.setIndentationRight(36);
            TreeSet<Movie> movies = new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            ListItem movieitem;
            for (Movie movie : movies) {
                movieitem = new ListItem(movie.getMovieTitle());
                movieitem.add(new Chunk(new VerticalPositionMark()));
                movieitem.add(new Chunk(String.valueOf(movie.getYear())));
                if (movie.getYear() > 1999) {
                    movieitem.add(PositionedArrow.RIGHT);
                }
                list.add(movieitem);
            }
            document.add(list);
        }
        document.close();
        connection.close();
    }
