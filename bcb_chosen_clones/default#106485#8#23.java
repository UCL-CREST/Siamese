    public static void main(String[] args) {
        System.out.println("Chapter 1 example 3: PageSize");
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0103.pdf"));
            document.open();
            for (int i = 0; i < 20; i++) {
                document.add(new Phrase("Hello World, Hello Sun, Hello Moon, Hello Stars, Hello Sea, Hello Land, Hello People. "));
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
