    protected void writePDF(String fileName) throws FileNotFoundException, DocumentException, Exception {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                theImage = getModelImage();
            }
        });
        if (theImage == null) throw new Exception("Error creating model image");
        if (theImage.width() <= theImage.height()) pdfDoc = new Document(PageSize.A4); else pdfDoc = new Document(PageSize.A4.rotate());
        writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileName));
        pdfDoc.addAuthor(UserNames.getLoggedInUser().getUsername());
        String creator = translate("aboutDlg.blurb");
        creator = creator.substring(0, creator.indexOf("\n")).trim();
        creator += " " + ExplicantoVersion.getVersion() + " " + ExplicantoVersion.getBuild();
        pdfDoc.addCreator(creator);
        pdfDoc.addProducer();
        pdfDoc.addTitle(editor.getPartName());
        writer.setPageEvent(pageListener);
        pdfDoc.open();
        createTitlePage();
        createModelLayoutPage();
        pdfDoc.close();
        tempFile.delete();
    }
