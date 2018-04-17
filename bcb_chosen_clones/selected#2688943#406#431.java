    public static void main(final String[] args) {
        System.out.println("Chapter 6: example FoobarStudyProgram");
        System.out.println("-> Creates a PDF file with a Study Program Table.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resource needed: studyprogram.xml");
        System.out.println("-> resulting PDF: studyprogram.pdf and studyprogram.htm");
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter06/studyprogram.pdf"));
            HtmlWriter.getInstance(document, new FileOutputStream("results/in_action/chapter06/studyprogram.htm"));
            document.open();
            Paragraph p = new Paragraph("Academic Year 2006-2007\n\n");
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            document.add(new FoobarStudyProgram("resources/in_action/chapter06/studyprogram.xml").getTable());
            p = new Paragraph("Sem.: 1 = first semester, 2 = second semester, Y = annual course");
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
            p = new Paragraph("P-T = courses can be taken on a part-time basis, 1 = first part, 2 = second part");
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
