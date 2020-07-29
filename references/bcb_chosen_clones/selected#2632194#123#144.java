    public static void main(String[] args) {
        System.out.println("Chapter 9: example peace in all languages");
        System.out.println("-> Creates a PDF file with a table containing");
        System.out.println("   the word peace in different languages.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: c:/windows/fonts/arialuni.ttf");
        System.out.println("->                   abserif4_5.ttf, damase.ttf and");
        System.out.println("                     fsex2p00_public.ttf");
        System.out.println("->                   the file peace.xml");
        Document doc = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("results/in_action/chapter09/peace.pdf"));
            doc.open();
            Peace p = new Peace();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(new FileInputStream("resources/in_action/chapter09/peace.xml")), p);
            doc.add(p.getTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.close();
    }
