    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String c1red = request.getParameter("c1red");
        String c1green = request.getParameter("c1green");
        String c1blue = request.getParameter("c1blue");
        String c2red = request.getParameter("c2red");
        String c2green = request.getParameter("c2green");
        String c2blue = request.getParameter("c2blue");
        String c3red = request.getParameter("c3red");
        String c3green = request.getParameter("c3green");
        String c3blue = request.getParameter("c3blue");
        Color border = new Color(0x00, 0x00, 0x00);
        Color bgTable = new Color(0x40, 0x00, 0xFF);
        Color bgCells = new Color(0xFF, 0xFF, 0x00);
        try {
            int i;
            i = Integer.parseInt(c1red);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            i = Integer.parseInt(c1green);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            i = Integer.parseInt(c1blue);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            if (c1red != null && c1green != null && c1blue != null) {
                border = new Color(Integer.parseInt(c1red), Integer.parseInt(c1green), Integer.parseInt(c1blue));
            }
        } catch (Exception e) {
        }
        try {
            int i;
            i = Integer.parseInt(c2red);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            i = Integer.parseInt(c2green);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            i = Integer.parseInt(c2blue);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            if (c2red != null && c2green != null && c2blue != null) {
                bgTable = new Color(Integer.parseInt(c2red), Integer.parseInt(c2green), Integer.parseInt(c2blue));
            }
        } catch (Exception e) {
        }
        try {
            int i;
            i = Integer.parseInt(c3red);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            i = Integer.parseInt(c3green);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            i = Integer.parseInt(c3blue);
            if (i < 0 || i > 255) {
                throw new Exception();
            }
            if (c3red != null && c3green != null && c3blue != null) {
                bgCells = new Color(Integer.parseInt(c3red), Integer.parseInt(c3green), Integer.parseInt(c3blue));
            }
        } catch (Exception e) {
        }
        String y = request.getParameter("year");
        int year;
        if (y != null) {
            year = Integer.parseInt(y);
            if (year < 2000 || year > 2034) {
                year = 2001;
            }
        } else {
            year = 2001;
        }
        Font font1 = new Font(Font.HELVETICA, 14, Font.NORMAL, border);
        Font font2 = new Font(Font.HELVETICA, 24, Font.BOLD, border);
        Paragraph newLine = new Paragraph("\n", font1);
        Anchor anchor = new Anchor("visit http://www.lowagie.com/iText/", font1);
        anchor.setReference("http://www.lowagie.com/iText/");
        Paragraph link = new Paragraph(anchor);
        link.setAlignment(Element.ALIGN_CENTER);
        int language;
        String l = request.getParameter("language");
        if (l == null || Integer.parseInt(l) == ENGLISH) {
            language = ENGLISH;
        } else {
            language = DUTCH;
        }
        Document document = new Document(PageSize.A4, 30, 30, 80, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.addSubject("This is a Calendar for the year " + year + ".");
            HeaderFooter header = new HeaderFooter(new Phrase("iText Calendar", font1), false);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setBorder(Rectangle.BOTTOM);
            header.setBorderColor(border);
            document.setHeader(header);
            HeaderFooter footer = new HeaderFooter(new Phrase("iText, a JAVA PDF library", font1), false);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(Rectangle.TOP);
            footer.setBorderColor(border);
            document.setFooter(footer);
            document.open();
            for (int i = 0; i < 12; i++) {
                Paragraph name = new Paragraph(MONTH[language][i] + " " + year, font2);
                name.setAlignment(Element.ALIGN_CENTER);
                document.add(name);
                document.add(newLine);
                try {
                    Image image = Image.getInstance(new URL(request.getParameter("img" + i)));
                    image.scaleToFit(450, 250);
                    image.setAlignment(Image.MIDDLE);
                    document.add(image);
                } catch (Exception e) {
                    document.add(new Paragraph(e.getMessage()));
                }
                document.add(newLine);
                Month month = new Month(i, year, DAY[language], border, bgTable, bgCells);
                month.setAlignment(Element.ALIGN_CENTER);
                document.add(month);
                document.add(link);
                document.newPage();
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
            de.printStackTrace();
        }
        document.close();
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }
