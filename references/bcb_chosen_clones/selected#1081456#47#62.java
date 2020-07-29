    public static void main(String args[]) {
        try {
            Document doc = new Document(PageSize.A4, 50, 50, 100, 72);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("pageNumbersWatermark.pdf"));
            writer.setPageEvent(new PageNumbersWatermark());
            doc.open();
            String text = "some padding text ";
            for (int k = 0; k < 10; ++k) text += text;
            Paragraph p = new Paragraph(text);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            doc.add(p);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
