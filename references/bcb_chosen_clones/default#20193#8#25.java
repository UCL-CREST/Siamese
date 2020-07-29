    public static void main(String[] args) {
        System.out.println("Chapter 1 example 2: PageSize");
        Rectangle pageSize = new Rectangle(144, 720);
        pageSize.setBackgroundColor(new java.awt.Color(0xFF, 0xFF, 0xDE));
        Document document = new Document(pageSize);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0102.pdf"));
            document.open();
            for (int i = 0; i < 5; i++) {
                document.add(new Paragraph("Hello World"));
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
