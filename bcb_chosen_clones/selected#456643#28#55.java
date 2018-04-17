    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        Image[] img = { Image.getInstance(String.format(RESOURCE, "0120903")), Image.getInstance(String.format(RESOURCE, "0290334")), Image.getInstance(String.format(RESOURCE, "0376994")), Image.getInstance(String.format(RESOURCE, "0348150")) };
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell("X-Men");
        PdfPCell cell = new PdfPCell(img[0]);
        table.addCell(cell);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("X2");
        cell = new PdfPCell(img[1], true);
        table.addCell(cell);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell("X-Men: The Last Stand");
        table.addCell(img[2]);
        table.addCell("Superman Returns");
        cell = new PdfPCell();
        img[3].setWidthPercentage(50);
        cell.addElement(img[3]);
        table.addCell(cell);
        table.completeRow();
        document.add(table);
        document.close();
    }
