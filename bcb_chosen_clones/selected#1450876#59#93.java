    public String getPDFReport(Properties ctx, ArrayList dataSource) throws OperationException {
        String filename = RandomStringGenerator.randomstring() + ".pdf";
        String dir = UDIFilePropertiesManager.getProperty().get(ctx, PropertiesConstant.UDI_HOME) + "/config/reports/pdf/";
        String filepath = dir + filename;
        this.dataSource = dataSource;
        if (dataSource == null) throw new OperationException("Cannot generate report! Cause: empty datasource");
        Rectangle dimension = getDocumentDimension();
        Document document = new Document(dimension, MARGIN, MARGIN, MARGIN, MARGIN);
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
            writer.setPageEvent(new PDFReportPageEventHelper());
            document.open();
            Paragraph title = new Paragraph();
            Paragraph subTitle = new Paragraph();
            if (reportTitle != null) {
                title.add(new Chunk(reportTitle, TITLE_FONT));
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
            }
            if (reportSubTitle != null) {
                subTitle.add(new Chunk(reportSubTitle, SUBTITLE_FONT));
                subTitle.setAlignment(Element.ALIGN_CENTER);
                document.add(subTitle);
            }
            if ((reportSubTitle != null) || (reportTitle != null)) {
                document.add(new Paragraph(" "));
            }
            writeDocument(document);
            document.close();
            writer.close();
            return "config/report/pdf/" + filename;
        } catch (Exception e) {
            throw new OperationException(e);
        }
    }
