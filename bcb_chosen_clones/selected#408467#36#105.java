    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Document document = new Document();
        try {
            OutputStream os = new FileOutputStream(RESULT);
            PdfWriter.getInstance(document, os);
            document.open();
            Session session = (Session) MySessionFactory.currentSession();
            Query q = session.createQuery("from FilmTitle order by title");
            java.util.List<FilmTitle> results = q.list();
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[] { 1, 5 });
            File f;
            Paragraph p;
            Chunk c;
            PdfPCell cell = new PdfPCell();
            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
            p = new Paragraph("FILMFESTIVAL", bold);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
            cell.setColspan(2);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setFixedHeight(20);
            cell.setColspan(2);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new Movies14().new PageCell());
            table.addCell(cell);
            table.setHeaderRows(2);
            table.setFooterRows(1);
            for (FilmTitle movie : results) {
                f = new File("resources/classroom/filmposters/" + movie.getFilmId() + ".jpg");
                if (f.exists()) {
                    cell = new PdfPCell(Image.getInstance(f.getPath()), true);
                    cell.setPadding(2);
                } else {
                    cell = new PdfPCell();
                }
                table.addCell(cell);
                p = new Paragraph(20);
                c = new Chunk(movie.getTitle(), bold);
                c.setAnchor("http://cinema.lowagie.com/titel.php?id=" + movie.getFilmId());
                p.add(c);
                c = new Chunk(" (" + movie.getYear() + ") ", italic);
                p.add(c);
                c = new Chunk("IMDB");
                c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
                p.add(c);
                cell = new PdfPCell();
                cell.setUseAscender(true);
                cell.setUseDescender(true);
                cell.addElement(p);
                Set<DirectorName> directors = movie.getDirectorNames();
                List list = new List();
                for (DirectorName director : directors) {
                    list.add(director.getName());
                }
                cell.addElement(list);
                table.addCell(cell);
            }
            document.add(table);
            document.close();
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        } catch (DocumentException e) {
            LOGGER.error("DocumentException: ", e);
        }
    }
