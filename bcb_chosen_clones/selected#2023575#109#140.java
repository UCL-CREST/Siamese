    private Document build() throws FileNotFoundException, DocumentException {
        Rectangle r = confiPdf.getPaperSizesRectangles()[confiPdf.getPaperSize()];
        if (confiPdf.getOrientation() > 0) r = r.rotate();
        Document document = new Document(r);
        if (nameFile != null) {
            PdfWriter.getInstance(document, new FileOutputStream(nameFile));
        }
        if (!confiPdf.getTitle().trim().equals("")) {
            document.addTitle(confiPdf.getTitle());
        }
        document.addSubject("Export pdf");
        document.open();
        if (!confiPdf.getTitle().trim().equals("")) {
            Phrase tituloPhrase = new Phrase(confiPdf.getTitle(), new Font(Font.TIMES_ROMAN, 14, Font.BOLD));
            document.add(tituloPhrase);
        }
        Table datatable;
        try {
            datatable = buildTable();
        } catch (BadElementException e) {
            document.close();
            return null;
        }
        if (datatable != null) {
            document.add(buildTable());
            document.close();
            return document;
        } else {
            document.close();
            return null;
        }
    }
