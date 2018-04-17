    private Document buid(String title) throws Exception {
        Rectangle r = PDFSetting.getInstance().getPageSize();
        Document document = new Document(r);
        if (!PDFSetting.getInstance().isVertical()) {
            document.setPageSize(document.getPageSize().rotate());
        }
        if (fileOut != null) {
            PdfWriter.getInstance(document, new FileOutputStream(fileOut));
        } else {
            throw new Exception("File is null");
        }
        document.open();
        document.addAuthor("LA_Azada");
        document.addSubject("Table");
        if (title != null && !title.trim().equals("")) {
            document.addTitle(title);
        }
        Table table;
        table = buildTable();
        if (table != null) {
            document.add(table);
        }
        document.close();
        return document;
    }
