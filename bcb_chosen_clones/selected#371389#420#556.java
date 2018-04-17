    public ByteArrayOutputStream exportAsPdfUserOnlyForSample() throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 10, 10, 20, 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        HeaderFooter header;
        if (name != null) header = new HeaderFooter(new Phrase("Data Collections for Sample: " + name + "  ---  Proposal: " + proposalCode + proposalNumber), false); else header = new HeaderFooter(new Phrase("Data Collections for Proposal: " + proposalCode + proposalNumber), false);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setBorderWidth(1);
        header.getBefore().getFont().setSize(8);
        HeaderFooter footer = new HeaderFooter(new Phrase("Page n."), true);
        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setBorderWidth(1);
        footer.getBefore().getFont().setSize(6);
        document.setHeader(header);
        document.setFooter(footer);
        document.open();
        if (dataCollectionList.isEmpty()) {
            document.add(new Paragraph("There is no data collection in this report"));
            document.close();
            return baos;
        }
        int NumColumns = 15;
        PdfPTable table = new PdfPTable(NumColumns);
        int headerW[] = { 5, 9, 8, 4, 5, 6, 5, 5, 5, 5, 6, 6, 6, 7, 18 };
        table.setWidths(headerW);
        table.setWidthPercentage(100);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setBorderWidth(1);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(new Paragraph("Beamline", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Start time", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Image prefix", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Run no", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("# images", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Wavelength\nÅ", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Distance\nmm", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Exp. time\ns", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Phi start\n°", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Phi range\n°", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Detector resol.\nÅ", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Beamsize Hor\nµm", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Beamsize Vert\nµm", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Transmission\n%", new Font(Font.HELVETICA, 8)));
        table.addCell(new Paragraph("Comments", new Font(Font.HELVETICA, 8)));
        table.setHeaderRows(1);
        table.getDefaultCell().setBorderWidth(1);
        DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        df1.applyPattern("#####0.0");
        DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        df2.applyPattern("#####0.00");
        DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        df3.applyPattern("#####0.000");
        DecimalFormat df4 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        df4.applyPattern("#####0.0000");
        SimpleDateFormat dtf = new SimpleDateFormat();
        dtf.applyPattern("dd/MM/yyyy HH:MM");
        Iterator it = dataCollectionList.iterator();
        int i = 1;
        while (it.hasNext()) {
            DataCollectionValue col = (DataCollectionValue) it.next();
            ClientLogger.getInstance().debug("table of datacollections per sample pdf ");
            if (col.getSession().getBeamLineName() != null) table.addCell(new Paragraph(col.getSession().getBeamLineName(), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getStartTime() != null) table.addCell(new Paragraph(dtf.format(col.getStartTime()), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getImagePrefix() != null) table.addCell(new Paragraph(col.getImagePrefix(), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getDataCollectionNumber() != null) table.addCell(new Paragraph(col.getDataCollectionNumber().toString(), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getNumberOfImages() != null) table.addCell(new Paragraph(col.getNumberOfImages().toString(), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getWavelength() != null) table.addCell(new Paragraph(df4.format(col.getWavelength()), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getDetectorDistance() != null) table.addCell(new Paragraph(df1.format(col.getDetectorDistance()), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getExposureTime() != null) table.addCell(new Paragraph(df3.format(col.getExposureTime()), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getAxisStart() != null) table.addCell(new Paragraph(df1.format(col.getAxisStart()).toString(), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getAxisRange() != null) table.addCell(new Paragraph(df1.format(col.getAxisRange()), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getResolution() != null) table.addCell(new Paragraph(df2.format(col.getResolution()), new Font(Font.HELVETICA, 8))); else table.addCell("");
            if (col.getBeamSizeHorizontal() != null) {
                Integer beamSizeHorizontalMicro = new Double(col.getBeamSizeHorizontal().doubleValue() * 1000).intValue();
                table.addCell(new Paragraph(beamSizeHorizontalMicro.toString(), new Font(Font.HELVETICA, 8)));
            } else table.addCell("");
            if (col.getBeamSizeVertical() != null) {
                Integer beamSizeVerticalMicro = new Double(col.getBeamSizeVertical().doubleValue() * 1000).intValue();
                table.addCell(new Paragraph(beamSizeVerticalMicro.toString(), new Font(Font.HELVETICA, 8)));
            } else table.addCell("");
            if (col.getTransmission() != null) {
                int tempTransmission = (new Double(col.getTransmission()).intValue());
                table.addCell(new Paragraph(new Integer(tempTransmission).toString(), new Font(Font.HELVETICA, 8)));
            } else table.addCell("");
            if (col.getComments() != null && col.getComments() != "") table.addCell(new Paragraph(col.getComments(), new Font(Font.HELVETICA, 8))); else table.addCell("");
            i++;
        }
        document.add(table);
        if (energyScanList.isEmpty()) {
            document.add(new Paragraph("There is no energy scan in this report"));
            document.close();
            return baos;
        }
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        int NumColumnsES = 10;
        PdfPTable tableES = new PdfPTable(NumColumnsES);
        int headerwidths[] = { 25, 11, 9, 8, 7, 7, 9, 7, 7, 10 };
        tableES.setWidths(headerwidths);
        tableES.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableES.setWidthPercentage(60);
        tableES.getDefaultCell().setPadding(3);
        tableES.getDefaultCell().setBorderWidth(1);
        tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableES.addCell(new Paragraph("Scan File", new Font(Font.HELVETICA, 8)));
        if (name != null) tableES.addCell(new Paragraph("Transmission Factor", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("Exposure Time", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("Peak Energy", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("f'", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("f\"", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("Inflexion Energy", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("f'", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("f\"", new Font(Font.HELVETICA, 8)));
        tableES.addCell(new Paragraph("X-ray Dose", new Font(Font.HELVETICA, 8)));
        tableES.setHeaderRows(1);
        tableES.getDefaultCell().setBorderWidth(1);
        Iterator itES = energyScanList.iterator();
        i = 1;
        while (itES.hasNext()) {
            EnergyScanLightValue col = (EnergyScanLightValue) itES.next();
            ClientLogger.getInstance().debug("table of energy scans pdf ");
            if (col.getScanFileFullPath() != null) tableES.addCell(new Paragraph(col.getScanFileFullPath(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getTransmissionFactor() != null) tableES.addCell(new Paragraph(df2.format(col.getTransmissionFactor()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getExposureTime() != null) tableES.addCell(new Paragraph(df2.format(col.getExposureTime()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getPeakEnergy() != null) tableES.addCell(new Paragraph(df2.format(col.getPeakEnergy()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getPeakFprime() != null) tableES.addCell(new Paragraph(df2.format(col.getPeakFprime()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getPeakFdoublePrime() != null) tableES.addCell(new Paragraph(df2.format(col.getPeakFdoublePrime()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getInflectionEnergy() != null) tableES.addCell(new Paragraph(df2.format(col.getInflectionEnergy()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getInflectionFprime() != null) tableES.addCell(new Paragraph(df2.format(col.getInflectionFprime()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getInflectionFdoublePrime() != null) tableES.addCell(new Paragraph(df2.format(col.getInflectionFdoublePrime()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            if (col.getXrayDose() != null) tableES.addCell(new Paragraph(df2.format(col.getXrayDose()), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
            i++;
        }
        document.add(tableES);
        document.close();
        return baos;
    }
