    public InputStream createReport(MovieCollection movieCollection, ExportPdfConfiguration exportConfiguration) throws MDFToPdfException {
        try {
            Document document = new Document(PageSize.A4.rotate(), 0f, 0f, 20f, 0f);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font boxTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 25f, Font.BOLD, CommonReportElements.TITLE_COLOR);
            Font collectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 15f, Font.BOLD, CommonReportElements.TITLE_COLOR);
            Font movieTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 7f, Font.BOLD);
            Font movieDescriptionFont = FontFactory.getFont(FontFactory.HELVETICA, 6f, Font.NORMAL);
            Font movieMetaDataFont = FontFactory.getFont(FontFactory.HELVETICA, 5f, Font.NORMAL, BaseColor.DARK_GRAY);
            FontConfiguration fontConfiguration = new FontConfiguration();
            fontConfiguration.setBoxTitleFont(boxTitleFont);
            fontConfiguration.setCollectionTitleFont(collectionTitleFont);
            fontConfiguration.setMovieDescriptionFont(movieDescriptionFont);
            fontConfiguration.setMovieMetaDataFont(movieMetaDataFont);
            fontConfiguration.setMovieTitleFont(movieTitleFont);
            Paragraph paragraph = new Paragraph();
            paragraph.add(getMainTable(movieCollection, fontConfiguration));
            document.add(paragraph);
            document.close();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            outputStream.close();
            return byteArrayInputStream;
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not create the specified File for writing the PDF");
            throw new MDFToPdfException(ERROR_TYPE.ERROR_WRITING_PDF_FILE, e);
        } catch (DocumentException e) {
            logger.log(Level.SEVERE, "Could not create the specified File for writing the PDF");
            throw new MDFToPdfException(ERROR_TYPE.ERROR_WRITING_PDF_FILE, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create the specified File for writing the PDF");
            throw new MDFToPdfException(ERROR_TYPE.ERROR_WRITING_PDF_FILE, e);
        }
    }
