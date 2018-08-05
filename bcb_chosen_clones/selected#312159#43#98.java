    public void execute() {
        try {
            if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
            File src = (File) getValue("srcfile");
            if (getValue("destfile1") == null) throw new InstantiationException("You need to choose a destination file for the first part of the PDF");
            File file1 = (File) getValue("destfile1");
            if (getValue("destfile2") == null) throw new InstantiationException("You need to choose a destination file for the second part of the PDF");
            File file2 = (File) getValue("destfile2");
            int pagenumber = Integer.parseInt((String) getValue("pagenumber"));
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            int n = reader.getNumberOfPages();
            System.out.println("There are " + n + " pages in the original file.");
            if (pagenumber < 2 || pagenumber > n) {
                throw new DocumentException("You can't split this document at page " + pagenumber + "; there is no such page.");
            }
            Document document1 = new Document(reader.getPageSizeWithRotation(1));
            Document document2 = new Document(reader.getPageSizeWithRotation(pagenumber));
            PdfWriter writer1 = PdfWriter.getInstance(document1, new FileOutputStream(file1));
            PdfWriter writer2 = PdfWriter.getInstance(document2, new FileOutputStream(file2));
            document1.open();
            PdfContentByte cb1 = writer1.getDirectContent();
            document2.open();
            PdfContentByte cb2 = writer2.getDirectContent();
            PdfImportedPage page;
            int rotation;
            int i = 0;
            while (i < pagenumber - 1) {
                i++;
                document1.setPageSize(reader.getPageSizeWithRotation(i));
                document1.newPage();
                page = writer1.getImportedPage(reader, i);
                rotation = reader.getPageRotation(i);
                if (rotation == 90 || rotation == 270) {
                    cb1.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
                } else {
                    cb1.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                }
            }
            while (i < n) {
                i++;
                document2.setPageSize(reader.getPageSizeWithRotation(i));
                document2.newPage();
                page = writer2.getImportedPage(reader, i);
                rotation = reader.getPageRotation(i);
                if (rotation == 90 || rotation == 270) {
                    cb2.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
                } else {
                    cb2.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                }
            }
            document1.close();
            document2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
