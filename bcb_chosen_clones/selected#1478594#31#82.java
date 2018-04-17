    public static void main(final String[] args) {
        System.out.println("Chapter 4: example FoxDogGeneric3");
        System.out.println("-> Creates a PDF file with some text");
        System.out.println("   of which some words are indexed automatically.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: fox_dog_generic3.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter04/fox_dog_generic3.pdf"));
            IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            document.open();
            create_0(document, index);
            create_1(document, index);
            document.newPage();
            create_1(document, index);
            document.newPage();
            create_2(document, index);
            document.newPage();
            create_3(document, index);
            document.newPage();
            create_1(document, index);
            document.newPage();
            document.add(new Paragraph("Index:"));
            List list = index.getSortedEntries();
            for (int i = 0, n = list.size(); i < n; i++) {
                IndexEvents.Entry entry = (IndexEvents.Entry) list.get(i);
                Paragraph in = new Paragraph();
                in.add(new Chunk(entry.getIn1()));
                if (entry.getIn2().length() > 0) {
                    in.add(new Chunk("; " + entry.getIn2()));
                }
                if (entry.getIn3().length() > 0) {
                    in.add(new Chunk(" (" + entry.getIn3() + ")"));
                }
                List pages = entry.getPagenumbers();
                List tags = entry.getTags();
                in.add(": ");
                for (int p = 0, x = pages.size(); p < x; p++) {
                    Chunk pagenr = new Chunk(" p" + pages.get(p));
                    pagenr.setLocalGoto((String) tags.get(p));
                    in.add(pagenr);
                }
                document.add(in);
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
