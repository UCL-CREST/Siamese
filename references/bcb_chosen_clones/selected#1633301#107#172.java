    public static void createPdf(String file, int type) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        Image img1 = Image.getInstance("resources/in_action/chapter05/fox.gif");
        Image img2 = Image.getInstance("resources/in_action/chapter05/dog.gif");
        Image img3 = Image.getInstance("resources/in_action/chapter05/hitchcock.gif");
        document.open();
        for (int i = 0; i < 20; i++) {
            document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
            document.newPage();
            if (type == 1 && i == 4) {
                document.add(new Paragraph("cache_start=A"));
                document.newPage();
                i++;
                document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
                document.add(img1);
                document.add(img3);
                document.newPage();
            }
            if (type == 1 && i == 6) {
                document.add(new Paragraph("cache_end=A"));
                document.newPage();
            }
            if (type == 1 && i == 10) {
                document.add(new Paragraph("cache_start=B"));
                document.newPage();
                i++;
                document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
                document.add(img2);
                document.newPage();
            }
            if (type == 1 && i == 12) {
                document.add(new Paragraph("cache_end=B"));
                document.newPage();
            }
            if (type == 2 && i == 3) {
                document.add(new Paragraph("cache_start=C"));
                document.newPage();
                i++;
                document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
                document.add(img3);
                document.newPage();
            }
            if (type == 2 && i == 8) {
                document.add(new Paragraph("cache_end=C"));
                document.newPage();
            }
            if (type == 2 && i == 14) {
                document.add(new Paragraph("replace=A"));
                document.newPage();
            }
            if (type == 3 && i == 6) {
                document.add(new Paragraph("replace=A"));
                document.newPage();
            }
            if (type == 3 && i == 14) {
                document.add(new Paragraph("replace=B"));
                document.newPage();
            }
            if (type == 3 && i == 16) {
                document.add(new Paragraph("replace=C"));
                document.newPage();
            }
        }
        document.close();
    }
