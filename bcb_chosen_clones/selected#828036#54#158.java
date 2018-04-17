    @Override
    protected boolean generateFiles(final String outputFile, final String input, final ArrayList<SegoviaStatistics> stats, final ArrayList<SegoviaStatistics> stats2, final String title, final String title2) {
        boolean res = true;
        try {
            Document document = new Document();
            String colorTitle = "#7f0000";
            String tableR = "table";
            StyleSheet st = new StyleSheet();
            st.loadTagStyle("body", " background-color", "#FFFFFF");
            st.loadTagStyle("body", "color", "#000000");
            st.loadTagStyle("body", "font-size", "9pt");
            st.loadTagStyle("h1", "margin", "5");
            st.loadTagStyle("h1", "padding", "0");
            st.loadTagStyle("h1", "color", colorTitle);
            st.loadTagStyle("h1", "font-size", "160%");
            st.loadTagStyle("h1", "font-weight", "bold");
            st.loadTagStyle("h2", "margin", "10");
            st.loadTagStyle("h2", "padding", "10");
            st.loadTagStyle("h2", "color", "#7f0000");
            st.loadTagStyle("h2", "font-size", "140%");
            st.loadTagStyle("h2", "font-weight", "bold");
            st.loadTagStyle("h2", "bordercolor", "#7f0000");
            st.loadTagStyle(tableR, "border", "0.5");
            st.loadTagStyle(tableR, "columns", "6");
            st.loadTagStyle(tableR, "bordercolor", colorTitle);
            st.loadTagStyle(tableR, "backgroundcolor", "#AAAAAA");
            StringReader stringReader = new StringReader(input);
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFile + ".pdf"));
            document.open();
            ArrayList p = HTMLWorker.parseToList(stringReader, st);
            for (int k = 0; k < p.size(); ++k) {
                boolean draw = true;
                ArrayList ar = ((Element) p.get(k)).getChunks();
                for (int t = 0; t < ar.size(); ++t) {
                    if (((Element) ar.get(t)).toString().equals("**IMAGE**") || ((Element) ar.get(t)).toString().equals("**IMAGE2**")) {
                        ChartGenerator chartG = null;
                        JFreeChart chart = null;
                        if (((Element) ar.get(t)).toString().equals("**IMAGE**")) {
                            chartG = new ChartGenerator(stats);
                            chart = chartG.createBarChart("Found " + title, title, "Num");
                        }
                        if (((Element) ar.get(t)).toString().equals("**IMAGE2**")) {
                            chartG = new ChartGenerator(stats2);
                            chart = chartG.createBarChart("Found " + title2, title2, "Num");
                        }
                        PdfContentByte dc = docWriter.getDirectContent();
                        PdfTemplate tp = dc.createTemplate(IMAGE_WIDTH, IMAGE_HEIGHT);
                        Graphics2D g2 = tp.createGraphics(IMAGE_WIDTH, IMAGE_HEIGHT, new DefaultFontMapper());
                        Rectangle2D r2D = new Rectangle2D.Double(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                        chart.draw(g2, r2D, null);
                        g2.dispose();
                        ImgTemplate component;
                        try {
                            component = new ImgTemplate(tp);
                            document.add(component);
                            draw = false;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                if (draw) {
                    document.add((Element) p.get(k));
                }
            }
            try {
                Image png, png2;
                docWriter.setStrictImageSequence(true);
                png = Image.getInstance(getClass().getResource("/reports/images/7_transparent.png").toURI().toURL());
                png2 = Image.getInstance(getClass().getResource("/reports/images/romulus_logo_transparent_black.png").toURI().toURL());
                png.setAlignment(Image.RIGHT | Image.TEXTWRAP);
                png2.setAlignment(Image.RIGHT | Image.TEXTWRAP);
                PdfPTable table = new PdfPTable(6);
                PdfPCell cell = new PdfPCell();
                PdfPCell cell2 = new PdfPCell();
                PdfPCell cellText = new PdfPCell();
                cellText.setColspan(4);
                cell.addElement(new Chunk(png, 0, 0));
                cell2.addElement(new Chunk(png2, 0, 0));
                cell.setBorder(0);
                cell2.setBorder(0);
                cellText.addElement(new Paragraph("Copyright 2009 - This document has been created by Segovia Report Tool" + " developed in the ICT Romulus Project"));
                cellText.addElement(new Paragraph("http.//www.ict-romulus.eu"));
                cellText.setBorder(0);
                cell.setBackgroundColor(new Color(BG_COLOR, BG_COLOR, BG_COLOR));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.setWidthPercentage(100);
                table.addCell(cellText);
                table.addCell(cell);
                table.addCell(cell2);
                document.add(table);
            } catch (URISyntaxException ex) {
                Logger.getLogger(PDFReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            document.close();
            res = true;
        } catch (IOException ex) {
            Logger.getLogger(PDFReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            res = false;
        } catch (DocumentException ex) {
            Logger.getLogger(PDFReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            res = false;
        }
        return res;
    }
