    public static void createTempPDFs() throws DocumentException, FileNotFoundException, IOException {
        LOG.info("Creating PDFs ");
        for (int i = 0; i < files.length; i++) {
            LOG.info(files[i]);
            Document document = new Document();
            LOG.info("0");
            PdfWriter.getInstance(document, new FileOutputStream(files[i]));
            LOG.info("1");
            document.open();
            LOG.info("2");
            document.add(new Paragraph("Random Message Goes Here so PDF will happily create itself"));
            LOG.info("3");
            document.close();
            LOG.info("4");
        }
        LOG.info("Finished Creating PDFs");
    }
