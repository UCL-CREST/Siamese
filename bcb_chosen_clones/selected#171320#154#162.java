    public void xtest11() throws Exception {
        PDFManager manager = new ITextManager();
        InputStream pdf = new FileInputStream("/tmp/UML2.pdf");
        InputStream page1 = manager.cut(pdf, 1, 1);
        OutputStream outputStream = new FileOutputStream("/tmp/page.pdf");
        IOUtils.copy(page1, outputStream);
        outputStream.close();
        pdf.close();
    }
