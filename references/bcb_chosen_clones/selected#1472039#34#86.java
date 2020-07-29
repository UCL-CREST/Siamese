    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setCompressionLevel(0);
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate celluloid = canvas.createTemplate(595, 84.2f);
        celluloid.rectangle(8, 8, 579, 68);
        for (float f = 8.25f; f < 581; f += 6.5f) {
            celluloid.roundRectangle(f, 8.5f, 6, 3, 1.5f);
            celluloid.roundRectangle(f, 72.5f, 6, 3, 1.5f);
        }
        celluloid.setGrayFill(0.1f);
        celluloid.eoFill();
        writer.releaseTemplate(celluloid);
        for (int i = 0; i < 10; i++) {
            canvas.addTemplate(celluloid, 0, i * 84.2f);
        }
        document.newPage();
        for (int i = 0; i < 10; i++) {
            canvas.addTemplate(celluloid, 0, i * 84.2f);
        }
        List<Movie> movies = PojoFactory.getMovies(connection);
        Image img;
        float x = 11.5f;
        float y = 769.7f;
        for (Movie movie : movies) {
            img = Image.getInstance(String.format(RESOURCE, movie.getImdb()));
            img.scaleToFit(1000, 60);
            img.setAbsolutePosition(x + (45 - img.getScaledWidth()) / 2, y);
            canvas.addImage(img);
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        document.newPage();
        canvas.addTemplate(celluloid, 0.8f, 0, 0.35f, 0.65f, 0, 600);
        Image tmpImage = Image.getInstance(celluloid);
        tmpImage.setAbsolutePosition(0, 480);
        document.add(tmpImage);
        tmpImage.setRotationDegrees(30);
        tmpImage.scalePercent(80);
        tmpImage.setAbsolutePosition(30, 500);
        document.add(tmpImage);
        tmpImage.setRotation((float) Math.PI / 2);
        tmpImage.setAbsolutePosition(200, 300);
        document.add(tmpImage);
        document.close();
        connection.close();
    }
