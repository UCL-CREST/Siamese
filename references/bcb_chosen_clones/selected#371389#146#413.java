    public ByteArrayOutputStream exportAsPdfUserOnly() throws Exception {
        Document document = new Document(PageSize.A4, 10, 10, 20, 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        HeaderFooter header;
        if (name != null) header = new HeaderFooter(new Phrase("Data Collections for Proposal: " + proposalCode + proposalNumber + "  ---  Sample: " + name, new Font(Font.HELVETICA, 10, Font.BOLD)), false); else header = new HeaderFooter(new Phrase("Data Collections for Proposal: " + proposalCode + proposalNumber + " on Beamline: " + slv.getBeamLineName() + "  ---  Session start date: " + Formatter.formatDate(slv.getStartDate()), new Font(Font.HELVETICA, 10, Font.BOLD)), false);
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
        if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
            document.add(new Paragraph("Crystallographer:", new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE)));
            document.add(new Paragraph(slv.getBeamLineOperator(), new Font(Font.HELVETICA, 8)));
        }
        document.add(new Paragraph(Constants.SESSION_VISIT_CAP + " comments:", new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE)));
        document.add(new Paragraph(slv.getComments(), new Font(Font.HELVETICA, 8)));
        ClientLogger.getInstance().debug("Table of Data Collections");
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Data Collections:", new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE)));
        document.add(new Paragraph(" "));
        if (dataCollectionList.isEmpty()) {
            document.add(new Paragraph("There is no data collection in this report", new Font(Font.HELVETICA, 8)));
        } else {
            int NumColumns = 12;
            PdfPTable table = new PdfPTable(NumColumns);
            int headerwidthsSession[] = { 12, 5, 6, 10, 7, 5, 5, 5, 6, 6, 8, 17 };
            int headerwidthsName[] = { 10, 5, 5, 6, 9, 7, 5, 5, 5, 6, 6, 7, 17 };
            int headerwidthsMXPress[] = { 12, 5, 6, 10, 7, 5, 5, 5, 6, 6, 7, 6, 17 };
            int headerwidthsNameMXPress[] = { 10, 5, 5, 6, 10, 6, 5, 5, 5, 6, 6, 7, 5, 17 };
            table.setWidths(headerwidthsSession);
            if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
                if (name != null) {
                    table = new PdfPTable(NumColumns + 2);
                    table.setWidths(headerwidthsNameMXPress);
                } else {
                    table = new PdfPTable(NumColumns + 1);
                    table.setWidths(headerwidthsMXPress);
                }
            } else {
                if (name != null) {
                    table = new PdfPTable(NumColumns + 1);
                    table.setWidths(headerwidthsName);
                } else {
                    table = new PdfPTable(NumColumns);
                    table.setWidths(headerwidthsSession);
                }
            }
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(3);
            table.getDefaultCell().setBorderWidth(1);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell();
            table.getDefaultCell().setGrayFill(0.6f);
            table.addCell(new Paragraph("Image prefix", new Font(Font.HELVETICA, 8)));
            if (name != null) table.addCell(new Paragraph("Beamline", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Run no", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("# images", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Wavelength angstrom", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Distance mm", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Exp. Time sec", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Phi start deg", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Phi range deg", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Xbeam mm", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Ybeam mm", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Detector resol. angstrom", new Font(Font.HELVETICA, 8)));
            if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) table.addCell(new Paragraph("Crystal class", new Font(Font.HELVETICA, 8)));
            table.addCell(new Paragraph("Comments", new Font(Font.HELVETICA, 8)));
            table.setHeaderRows(1);
            table.getDefaultCell().setBorderWidth(1);
            DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            df2.applyPattern("#####0.00");
            DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            df3.applyPattern("#####0.000");
            SessionFacadeLocal session = SessionFacadeUtil.getLocalHome().create();
            Iterator it = dataCollectionList.iterator();
            int i = 1;
            while (it.hasNext()) {
                DataCollectionFullValue col = (DataCollectionFullValue) it.next();
                SessionLightValue slv = session.findByPrimaryKeyLight(col.getSessionId());
                if (col.getNumberOfImages() != null) {
                    if (col.getNumberOfImages().intValue() > 5) {
                        table.getDefaultCell().setGrayFill(0.8f);
                    } else table.getDefaultCell().setGrayFill(0.99f);
                }
                if (col.getImagePrefix() != null) table.addCell(new Paragraph(col.getImagePrefix(), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (name != null) {
                    if (slv.getBeamLineName() != null) table.addCell(new Paragraph(slv.getBeamLineName(), new Font(Font.HELVETICA, 8))); else table.addCell("");
                }
                if (col.getDataCollectionNumber() != null) table.addCell(new Paragraph(col.getDataCollectionNumber().toString(), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getNumberOfImages() != null) table.addCell(new Paragraph(col.getNumberOfImages().toString(), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getWavelength() != null) table.addCell(new Paragraph(df3.format(col.getWavelength()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getDetectorDistance() != null) table.addCell(new Paragraph(df2.format(col.getDetectorDistance()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getExposureTime() != null) table.addCell(new Paragraph(df2.format(col.getExposureTime()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getAxisStart() != null) table.addCell(new Paragraph(df2.format(col.getAxisStart()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getAxisRange() != null) table.addCell(new Paragraph(df2.format(col.getAxisRange()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getXbeam() != null) table.addCell(new Paragraph(df2.format(col.getXbeam()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getYbeam() != null) table.addCell(new Paragraph(df2.format(col.getYbeam()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (col.getResolution() != null) table.addCell(new Paragraph(df2.format(col.getResolution()), new Font(Font.HELVETICA, 8))); else table.addCell("");
                if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
                    if (col.getCrystalClass() != null && col.getCrystalClass() != "") table.addCell(new Paragraph(col.getCrystalClass(), new Font(Font.HELVETICA, 8))); else table.addCell("");
                }
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                if (col.getComments() != null && col.getComments() != "") table.addCell(new Paragraph(col.getComments(), new Font(Font.HELVETICA, 8))); else table.addCell("");
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                i++;
            }
            document.add(table);
        }
        ClientLogger.getInstance().debug("Table of Energy Scans");
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Energy Scans:", new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE)));
        document.add(new Paragraph(" "));
        if (energyScanList.isEmpty()) {
            document.add(new Paragraph("There is no energy scan in this report", new Font(Font.HELVETICA, 8)));
        } else {
            int NumColumnsES = 12;
            PdfPTable tableES = new PdfPTable(NumColumnsES);
            int headerwidthsSessionES[] = { 7, 7, 7, 7, 7, 7, 6, 6, 7, 7, 7, 25 };
            int headerwidthsMXPressES[] = { 7, 7, 7, 7, 7, 7, 6, 6, 7, 7, 7, 6, 17 };
            tableES.setWidths(headerwidthsSessionES);
            if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
                tableES = new PdfPTable(NumColumnsES + 1);
                tableES.setWidths(headerwidthsMXPressES);
            }
            tableES.setWidthPercentage(100);
            tableES.getDefaultCell().setPadding(3);
            tableES.getDefaultCell().setBorderWidth(1);
            tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableES.getDefaultCell().setGrayFill(0.6f);
            tableES.addCell(new Paragraph("Element", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Inflection Energy (keV)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Exposure Time (s)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Inflection f' (e)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Inflection f'' (e)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Peak Energy (keV)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Peak f' (e)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Peak f'' (e)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Beam size Hor. (�m)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Beam size Ver. (�m)", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Transm. Factor (%)", new Font(Font.HELVETICA, 8)));
            if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) tableES.addCell(new Paragraph("Crystal class", new Font(Font.HELVETICA, 8)));
            tableES.addCell(new Paragraph("Comments", new Font(Font.HELVETICA, 8)));
            tableES.setHeaderRows(1);
            tableES.getDefaultCell().setGrayFill(0.99f);
            tableES.getDefaultCell().setBorderWidth(1);
            Iterator itES = energyScanList.iterator();
            while (itES.hasNext()) {
                EnergyScanLightValue col = (EnergyScanLightValue) itES.next();
                if (col.getElement() != null) tableES.addCell(new Paragraph(col.getElement().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getExposureTime() != null) tableES.addCell(new Paragraph(col.getExposureTime().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getInflectionEnergy() != null) tableES.addCell(new Paragraph(col.getInflectionEnergy().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getInflectionFprime() != null) tableES.addCell(new Paragraph(col.getInflectionFprime().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getInflectionFdoublePrime() != null) tableES.addCell(new Paragraph(col.getInflectionFdoublePrime().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getPeakEnergy() != null) tableES.addCell(new Paragraph(col.getPeakEnergy().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getPeakFprime() != null) tableES.addCell(new Paragraph(col.getPeakFprime().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getPeakFdoublePrime() != null) tableES.addCell(new Paragraph(col.getPeakFdoublePrime().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getBeamSizeHorizontal() != null) tableES.addCell(new Paragraph(col.getBeamSizeHorizontal().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getBeamSizeVertical() != null) tableES.addCell(new Paragraph(col.getBeamSizeVertical().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (col.getTransmissionFactor() != null) tableES.addCell(new Paragraph(col.getTransmissionFactor().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
                    if (col.getCrystalClass() != null) tableES.addCell(new Paragraph(col.getCrystalClass().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                }
                tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                if (col.getComments() != null) tableES.addCell(new Paragraph(col.getComments().toString(), new Font(Font.HELVETICA, 8))); else tableES.addCell("");
                tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            document.add(tableES);
        }
        ClientLogger.getInstance().debug("Table of XRF Spectra");
        document.add(new Paragraph(" "));
        document.add(new Paragraph("XRF Spectra:", new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE)));
        document.add(new Paragraph(" "));
        if (xfeList.isEmpty()) {
            document.add(new Paragraph("There is no XRF spectra in this report", new Font(Font.HELVETICA, 8)));
        } else {
            int NumColumnsXRF = 6;
            PdfPTable tableXRF = new PdfPTable(NumColumnsXRF);
            int headerwidthsSessionXRF[] = { 15, 15, 15, 15, 15, 25 };
            int headerwidthsMXPressXRF[] = { 16, 16, 15, 15, 15, 6, 17 };
            tableXRF.setWidths(headerwidthsSessionXRF);
            if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
                tableXRF = new PdfPTable(NumColumnsXRF + 1);
                tableXRF.setWidths(headerwidthsMXPressXRF);
            }
            tableXRF.setWidthPercentage(100);
            tableXRF.getDefaultCell().setPadding(3);
            tableXRF.getDefaultCell().setBorderWidth(1);
            tableXRF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableXRF.getDefaultCell().setGrayFill(0.6f);
            tableXRF.addCell(new Paragraph("Energy (keV)", new Font(Font.HELVETICA, 8)));
            tableXRF.addCell(new Paragraph("Exposure Time (s)", new Font(Font.HELVETICA, 8)));
            tableXRF.addCell(new Paragraph("Beam size Hor. (�m)", new Font(Font.HELVETICA, 8)));
            tableXRF.addCell(new Paragraph("Beam size Ver. (�m)", new Font(Font.HELVETICA, 8)));
            tableXRF.addCell(new Paragraph("Transm. Factor (%)", new Font(Font.HELVETICA, 8)));
            if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) tableXRF.addCell(new Paragraph("Crystal class", new Font(Font.HELVETICA, 8)));
            tableXRF.addCell(new Paragraph("Comments", new Font(Font.HELVETICA, 8)));
            tableXRF.setHeaderRows(1);
            tableXRF.getDefaultCell().setGrayFill(0.99f);
            tableXRF.getDefaultCell().setBorderWidth(1);
            Iterator itES = xfeList.iterator();
            while (itES.hasNext()) {
                XfefluorescenceSpectrumLightValue col = (XfefluorescenceSpectrumLightValue) itES.next();
                if (col.getEnergy() != null) tableXRF.addCell(new Paragraph(col.getEnergy().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                if (col.getExposureTime() != null) tableXRF.addCell(new Paragraph(col.getExposureTime().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                if (col.getBeamSizeHorizontal() != null) tableXRF.addCell(new Paragraph(col.getBeamSizeHorizontal().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                if (col.getBeamSizeVertical() != null) tableXRF.addCell(new Paragraph(col.getBeamSizeVertical().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                if (col.getBeamTransmission() != null) tableXRF.addCell(new Paragraph(col.getBeamTransmission().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                if (proposalCode.equals(Constants.PROPOSAL_CODE_FX)) {
                    if (col.getCrystalClass() != null) tableXRF.addCell(new Paragraph(col.getCrystalClass().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                }
                tableXRF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                if (col.getComments() != null) tableXRF.addCell(new Paragraph(col.getComments().toString(), new Font(Font.HELVETICA, 8))); else tableXRF.addCell("");
                tableXRF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            document.add(tableXRF);
        }
        if (proposalCode.equals(Constants.PROPOSAL_CODE_FX) && name == null) {
            ClientLogger.getInstance().debug("Table of Crystal Classes ");
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Summary:", new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE)));
            document.add(new Paragraph(" "));
            int NumColumnsCC = 2;
            PdfPTable tableCC = new PdfPTable(NumColumnsCC);
            int headerwidthsCC[] = { 15, 15 };
            tableCC.setWidths(headerwidthsCC);
            tableCC.setWidthPercentage(50);
            tableCC.getDefaultCell().setPadding(3);
            tableCC.getDefaultCell().setBorderWidth(1);
            tableCC.getDefaultCell().setGrayFill(0.6f);
            tableCC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableCC.addCell(new Paragraph("Crystal class", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph("Number of crystals", new Font(Font.HELVETICA, 8)));
            tableCC.setHeaderRows(1);
            tableCC.getDefaultCell().setGrayFill(0.99f);
            tableCC.addCell(new Paragraph("Collect (C)", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph(new Integer(nbCrystal1).toString(), new Font(Font.HELVETICA, 8)));
            ClientLogger.getInstance().debug("classe 1 : collect ");
            tableCC.addCell(new Paragraph("Specified Collect (SC)", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph(new Integer(nbCrystal2).toString(), new Font(Font.HELVETICA, 8)));
            ClientLogger.getInstance().debug("classe 2 : Specified Collect");
            tableCC.addCell(new Paragraph("Puck Screens (PS)", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph(new Integer(nbCrystal3).toString(), new Font(Font.HELVETICA, 8)));
            ClientLogger.getInstance().debug("classe 3 Puck Screens");
            tableCC.addCell(new Paragraph("Test (T)", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph(new Integer(nbCrystalT).toString(), new Font(Font.HELVETICA, 8)));
            ClientLogger.getInstance().debug("classe T Test");
            tableCC.addCell(new Paragraph("Energy Scan (E)", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph(new Integer(nbCrystalE).toString(), new Font(Font.HELVETICA, 8)));
            ClientLogger.getInstance().debug("classe E Energy Scan");
            tableCC.addCell(new Paragraph("XRF Spectra (X)", new Font(Font.HELVETICA, 8)));
            tableCC.addCell(new Paragraph(new Integer(nbCrystalX).toString(), new Font(Font.HELVETICA, 8)));
            ClientLogger.getInstance().debug("classe X XRF Spectra");
            document.add(tableCC);
            int nbPuckScreen = nbCrystal3;
            int nbTotal = nbCrystal1 + nbCrystal2 + nbCrystalT;
            document.add(new Paragraph("Total number of tests: " + new String(new Integer(nbTotal).toString()), new Font(Font.HELVETICA, 8)));
            document.add(new Paragraph("Nb of puck screens: " + new String(new Integer(nbPuckScreen).toString()), new Font(Font.HELVETICA, 8)));
            document.add(new Paragraph("Total number of samples: " + new String(new Integer(nbTotal + (nbPuckScreen * 10)).toString()), new Font(Font.HELVETICA, 8)));
        }
        document.close();
        return baos;
    }
