    public static void main(String[] args) {
        System.out.println("Chapter 1 example 4: Margins");
        Document document = new Document(PageSize.A5, 36, 72, 108, 180);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0104.pdf"));
            document.open();
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            for (int i = 0; i < 20; i++) {
                paragraph.add("Hello World, Hello Sun, Hello Moon, Hello Stars, Hello Sea, Hello Land, Hello People. ");
            }
            document.add(paragraph);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
