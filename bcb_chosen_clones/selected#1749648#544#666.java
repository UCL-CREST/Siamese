    public static String fidelityCard(Properties ctx, ArrayList<CustomerBean> customerList) throws OperationException {
        String reportName = RandomStringGenerator.randomstring() + ".pdf";
        String reportPath = ReportManager.getReportPath(reportName);
        boolean shouldPrintCard = false;
        for (CustomerBean b : customerList) {
            if (b.getIsActive()) {
                shouldPrintCard = true;
                break;
            }
        }
        if (!shouldPrintCard) {
            throw new NoCustomerFoundException("Cannot print fidelity card. Cause no active customers were found.");
        }
        Document document = new Document(PageSize.A4, 3, 3, 2, 4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(reportPath));
            document.open();
            PdfPTable main = new PdfPTable(2);
            main.setWidthPercentage(71.0f);
            main.getDefaultCell().setBorderColor(Color.gray);
            PdfPCell cell = new PdfPCell();
            cell.setMinimumHeight(150.0f);
            Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD);
            for (CustomerBean bean : customerList) {
                if (bean.getIsActive()) {
                    String name = bean.getPartnerName();
                    String name1 = "";
                    String add2 = "";
                    String add1 = "";
                    String city = "";
                    if (bean.getAddress1() != null) add1 = bean.getAddress1();
                    if (bean.getAddress2() != null) add2 = bean.getAddress2();
                    if (bean.getCity() != null) city = bean.getCity();
                    String Address = "   " + add1;
                    String Add2 = " " + add2;
                    String Add3 = "   " + city;
                    String BackPriv1Path = PathInfo.PROJECT_HOME + "/images/BackPriv1.jpg";
                    String backPriv2Path = PathInfo.PROJECT_HOME + "/images/backPriv2.jpg";
                    String frontImgPath = PathInfo.PROJECT_HOME + "/images/pc.png";
                    float WIDTH = 205;
                    float HEIGHT = 135;
                    Image Back1 = Image.getInstance(BackPriv1Path);
                    Back1.scaleAbsolute(WIDTH - 40, HEIGHT / 3);
                    Image Back2 = Image.getInstance(backPriv2Path);
                    Back2.scaleAbsolute(WIDTH, HEIGHT / 3);
                    Image frontImg = Image.getInstance(frontImgPath);
                    frontImg.scaleAbsolute(WIDTH, HEIGHT);
                    if (bean.getSurname() != null && bean.getSurname().trim().length() > 0) name1 = "   " + name + " " + name1 + bean.getSurname();
                    byte[] barcode = BarcodeManager.getBarcodeAsByte(bean.getBpartnerId().toString());
                    Image barcodeImg = Image.getInstance(barcode);
                    barcodeImg.setRotation(1.57f);
                    barcodeImg.scaleAbsolute(HEIGHT - 55f, WIDTH / 5);
                    PdfPTable card = new PdfPTable(2);
                    card.getDefaultCell().setBorderWidth(0.0f);
                    PdfPCell c = null;
                    card.setWidthPercentage(50.0f);
                    PdfPTable t = new PdfPTable(1);
                    PdfPTable nametable = new PdfPTable(1);
                    c = new PdfPCell(Back1);
                    c.setBorderWidth(0.0f);
                    nametable.addCell(c);
                    c = new PdfPCell(new Phrase(name1, smallFont));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_CENTER);
                    c.setBorderWidth(0.0f);
                    nametable.addCell(c);
                    c = new PdfPCell(new Phrase(Address, smallFont));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_CENTER);
                    c.setBorderWidth(0.0f);
                    nametable.addCell(c);
                    c = new PdfPCell(new Phrase(Add2, smallFont));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_CENTER);
                    c.setBorderWidth(0.0f);
                    nametable.addCell(c);
                    c = new PdfPCell(new Phrase(Add3, smallFont));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_CENTER);
                    c.setBorderWidth(0.0f);
                    nametable.addCell(c);
                    nametable.getDefaultCell().setBorderWidth(0.0f);
                    nametable.setHorizontalAlignment(Element.ALIGN_CENTER);
                    card.addCell(nametable);
                    c = new PdfPCell(barcodeImg);
                    c.setBorderWidth(0.0f);
                    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setPadding(5.0f);
                    card.addCell(c);
                    c = new PdfPCell(Back2);
                    c.setBorderWidth(0.0f);
                    c.setColspan(2);
                    card.addCell(c);
                    c = new PdfPCell(new Phrase(name1, smallFont));
                    c.setBorderWidth(0.0f);
                    t.addCell(c);
                    c = new PdfPCell(new Phrase(Address, smallFont));
                    c.setBorderWidth(0.0f);
                    t.addCell(c);
                    c = new PdfPCell(new Phrase(Add3, smallFont));
                    c.setBorderWidth(0.0f);
                    t.addCell(c);
                    PdfPTable card1 = new PdfPTable(1);
                    card.getDefaultCell().setBorderWidth(0.0f);
                    PdfPCell c1 = null;
                    card.setWidthPercentage(50.0f);
                    c1 = new PdfPCell(frontImg);
                    c1.setBorderWidth(0.0f);
                    card1.addCell(c1);
                    main.addCell(card);
                    main.addCell(card1);
                }
            }
            document.add(main);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
        return reportName;
    }
