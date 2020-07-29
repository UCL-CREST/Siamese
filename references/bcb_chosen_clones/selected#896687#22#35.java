    public static void main(String[] args) {
        Document document = new Document(PageSize.A4, 50, 50, 70, 70);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("endpage.pdf"));
            writer.setPageEvent(new EndPage());
            document.open();
            String text = "Lots of text. ";
            for (int k = 0; k < 10; ++k) text += text;
            document.add(new Paragraph(text));
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
