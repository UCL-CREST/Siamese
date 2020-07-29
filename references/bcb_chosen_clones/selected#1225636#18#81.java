    public void test() {
        System.out.println("document.add(BigTable)");
        Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("HelloWorld2.pdf"));
            document.open();
            String[] bogusData = { "", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "Comm." };
            int NumColumns = 10;
            PdfPTable datatable = new PdfPTable(NumColumns);
            int headerwidths[] = { 4, 4, 4, 4, 4, 4, 4, 4, 4, 16 };
            datatable.setWidths(headerwidths);
            datatable.setWidthPercentage(100);
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            datatable.addCell("Date");
            PdfPTable ins = new PdfPTable(4);
            ins.setHorizontalAlignment(Element.ALIGN_CENTER);
            ins.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell p1 = new PdfPCell();
            p1.setPhrase(new Phrase("Insulin"));
            p1.setHorizontalAlignment(Element.ALIGN_CENTER);
            p1.setColspan(4);
            ins.addCell(p1);
            ins.addCell("Z");
            ins.addCell("K");
            ins.addCell("V");
            ins.addCell("N");
            PdfPCell m = new PdfPCell(ins);
            m.setColspan(4);
            PdfPTable bg = new PdfPTable(4);
            bg.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell p2 = new PdfPCell();
            p2.setPhrase(new Phrase("Blood Glucose"));
            p2.setHorizontalAlignment(Element.ALIGN_CENTER);
            p2.setColspan(4);
            bg.addCell(p2);
            bg.addCell("Z");
            bg.addCell("K");
            bg.addCell("V");
            bg.addCell("N");
            PdfPCell m2 = new PdfPCell(bg);
            m2.setColspan(4);
            datatable.addCell(m);
            datatable.addCell(m2);
            datatable.addCell("Comment");
            datatable.setHeaderRows(1);
            datatable.getDefaultCell().setBorderWidth(1);
            for (int i = 1; i < 32; i++) {
                if (i % 2 == 1) {
                    datatable.getDefaultCell().setGrayFill(0.9f);
                }
                datatable.addCell(i + ".2");
                for (int x = 1; x < NumColumns; x++) {
                    datatable.addCell(bogusData[x]);
                }
                if (i % 2 == 1) {
                    datatable.getDefaultCell().setGrayFill(0.0f);
                }
            }
            document.add(datatable);
        } catch (Exception de) {
            de.printStackTrace();
        }
        document.close();
    }
