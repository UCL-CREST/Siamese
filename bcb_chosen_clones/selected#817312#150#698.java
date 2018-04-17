    private void fnExportToPDF(HttpServletRequest req, HttpServletResponse res) throws Exception {
        System.out.println("Inside fnExportToPDF");
        HttpSession session = req.getSession();
        if ("AESTAMP".equals(AppConfigManager.getProperty("BATCH-PRINTING-OF-BILLING-STATEMENT-FORMAT-OPTION"))) {
            try {
                CusBillingStatmentPrintBatch cbspb = (CusBillingStatmentPrintBatch) session.getAttribute("cust-billing-stmt-batch");
                Vector vecCustId = cbspb.getCustId();
                if (vecCustId.size() > 0) {
                    TreeMap treeStmt = cbspb.treeStmt;
                    TreeMap treeAging = cbspb.treeAging;
                    res.setContentType("text/pdf");
                    res.setHeader("Content-disposition", "filename=billStmt.pdf");
                    BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
                    Document document = new Document();
                    PdfWriter.getInstance(document, outputStream);
                    document.open();
                    Font tableFont = FontFactory.getFont("Helvetica", 12);
                    Font tableFontBold = FontFactory.getFont("Helvetica", 12, Font.BOLD, Color.BLACK);
                    float padding = 2f;
                    float leading = 10f;
                    Rectangle noBorder = new Rectangle(0f, 0f);
                    noBorder.setBorderWidthLeft(0f);
                    noBorder.setBorderWidthBottom(0f);
                    noBorder.setBorderWidthRight(0f);
                    noBorder.setBorderWidthTop(0f);
                    Rectangle withBorder = new Rectangle(0f, 0f);
                    withBorder.setBorderWidthLeft(0f);
                    withBorder.setBorderWidthBottom(1f);
                    withBorder.setBorderWidthRight(0f);
                    withBorder.setBorderWidthTop(1f);
                    Rectangle withFullBorder = new Rectangle(0f, 0f);
                    withFullBorder.setBorderWidthLeft(1f);
                    withFullBorder.setBorderWidthBottom(1f);
                    withFullBorder.setBorderWidthRight(1f);
                    withFullBorder.setBorderWidthTop(1f);
                    for (int cnt2 = 0; cnt2 < vecCustId.size(); cnt2++) {
                        Integer iCustPkid = (Integer) vecCustId.get(cnt2);
                        System.out.println("iCustPkid : " + iCustPkid.toString());
                        String custPkid = iCustPkid.toString();
                        String month = cbspb.getMonth();
                        Timestamp tsCurrent = TimeFormat.getTimestamp();
                        CustBillingStmtObject cbsObj = (CustBillingStmtObject) treeStmt.get(iCustPkid);
                        if (cbsObj != null) {
                            Timestamp tsMonth = TimeFormat.createTimestamp(month);
                            BigDecimal runningBal = new BigDecimal("0.00");
                            Integer branchId = cbspb.getBranch();
                            if (branchId.intValue() > 0) {
                                BranchObject branch = BranchNut.getObject(cbspb.getBranch());
                                if (branch != null) {
                                    Paragraph address1 = new Paragraph(branch.name + " (" + branch.regNo + ")", tableFont);
                                    if (branch.addr1.length() > 1) address1.add("\n" + branch.addr1);
                                    if (branch.addr2.length() > 1) address1.add("\n" + branch.addr2);
                                    if (branch.addr3.length() > 1) address1.add("\n" + branch.addr3);
                                    if (branch.zip.length() > 1) address1.add("\n" + branch.zip);
                                    if (branch.state.length() > 1) address1.add(" " + branch.state);
                                    if (branch.phoneNo.length() > 1) address1.add("\n" + branch.phoneNo);
                                    if (branch.faxNo.length() > 1) address1.add(" " + branch.faxNo);
                                    address1.setAlignment(Element.ALIGN_CENTER);
                                    document.add(address1);
                                    document.add(new Paragraph(" "));
                                }
                            }
                            Chunk cHeader2 = new Chunk("S T A T E M E N T" + "   " + "O F" + "   " + "A C C O U N T", tableFontBold);
                            cHeader2.setUnderline(0.2f, -2f);
                            Paragraph header2 = new Paragraph("");
                            header2.add(cHeader2);
                            header2.setAlignment(Element.ALIGN_CENTER);
                            document.add(header2);
                            document.add(new Paragraph(" "));
                            String strDetails1 = "To: " + cbsObj.mCustAccObj.name + " " + cbsObj.mCustAccObj.identityNumber;
                            if (cbsObj.mCustAccObj != null) {
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainAddress1;
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainAddress2;
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainAddress3;
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainCity + " " + cbsObj.mCustAccObj.mainPostcode;
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainState + " " + cbsObj.mCustAccObj.mainCountry;
                                if (cbsObj.mCustAccObj.telephone1.length() > 7) strDetails1 += "\n" + "Tel1: " + cbsObj.mCustAccObj.telephone1;
                                if (cbsObj.mCustAccObj.faxNo.length() > 4) strDetails1 += ", Fax: " + cbsObj.mCustAccObj.faxNo;
                                if (cbsObj.mCustAccObj.telephone2.length() > 4) strDetails1 += "\n" + "Tel2: " + cbsObj.mCustAccObj.telephone2;
                                if (cbsObj.mCustAccObj.mobilePhone.length() > 4) strDetails1 += ", Mobile: " + cbsObj.mCustAccObj.mobilePhone;
                            }
                            String strDetails2 = "Statement Date : " + tsCurrent.toString();
                            strDetails2 += "\n\n" + "Month 	 	        : " + TimeFormat.format(tsMonth, "yyyy MMMM");
                            strDetails2 += "\n\n" + "Customer Code : " + cbsObj.mCustAccObj.pkid.toString();
                            strDetails2 += "\n\n" + "Salesman   	    : " + UserNut.getUserName(cbsObj.mCustAccObj.salesman);
                            PdfPTable tDetails = new PdfPTable(2);
                            tDetails.setWidthPercentage(100f);
                            tDetails.addCell(makeCell(strDetails1, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            tDetails.addCell(makeCell(strDetails2, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            document.add(tDetails);
                            document.add(new Paragraph(" "));
                            PdfPTable tEntries = new PdfPTable(5);
                            tEntries.setWidthPercentage(100f);
                            tEntries.addCell(makeCell("Date", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                            tEntries.addCell(makeCell("Description", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                            tEntries.addCell(makeCell("Debit", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                            tEntries.addCell(makeCell("Credit", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                            tEntries.addCell(makeCell("Balance", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                            runningBal = runningBal.add(cbsObj.bdPrevBalance);
                            tEntries.addCell(makeCell(TimeFormat.strDisplayDate(cbsObj.mDateFrom), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            tEntries.addCell(makeCell("Previous Balance", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            if (cbsObj.bdPrevBalance.signum() > 0) {
                                tEntries.addCell(makeCell(CurrencyFormat.strCcy(cbsObj.bdPrevBalance.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            } else {
                                tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                tEntries.addCell(makeCell(CurrencyFormat.strCcy(cbsObj.bdPrevBalance.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            }
                            tEntries.addCell(makeCell(CurrencyFormat.strCcy(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            for (int cnt1 = 0; cnt1 < cbsObj.vecNominalAccountTxn.size(); cnt1++) {
                                NominalAccountTxnObject natObj = (NominalAccountTxnObject) cbsObj.vecNominalAccountTxn.get(cnt1);
                                Timestamp tsDate = TimeFormat.getTimestamp();
                                if (natObj.timeOption1.equals(NominalAccountTxnBean.TIME_STMT)) {
                                    tsDate = natObj.timeParam1;
                                }
                                if (natObj.timeOption2.equals(NominalAccountTxnBean.TIME_STMT)) {
                                    tsDate = natObj.timeParam2;
                                }
                                runningBal = runningBal.add(natObj.amount);
                                tEntries.addCell(makeCell(TimeFormat.strDisplayDate(tsDate), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                tEntries.addCell(makeCell(docPrefix(natObj.foreignTable, natObj.foreignKey.toString()), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                if (natObj.amount.signum() > 0) {
                                    tEntries.addCell(makeCell(CurrencyFormat.strCcy(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                } else {
                                    tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    tEntries.addCell(makeCell(CurrencyFormat.strCcy(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                }
                                tEntries.addCell(makeCell(CurrencyFormat.strCcy(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            }
                            tEntries.addCell(makeCell(TimeFormat.strDisplayDate(cbsObj.mDateTo), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            tEntries.addCell(makeCell("Latest Balance", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            if (runningBal.signum() > 0) {
                                tEntries.addCell(makeCell(CurrencyFormat.strCcy(runningBal.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            } else {
                                tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                tEntries.addCell(makeCell(CurrencyFormat.strCcy(runningBal.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            }
                            tEntries.addCell(makeCell(CurrencyFormat.strCcy(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                            document.add(tEntries);
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            CustStmtOfAccountForm stmtForm = (CustStmtOfAccountForm) treeAging.get(iCustPkid);
                            ;
                            BigDecimal accumBalance = new BigDecimal(0);
                            if (stmtForm != null) {
                                CustStmtOfAccountForm.StmtOfAcc theStmt = stmtForm.getStmtReport();
                                if (theStmt != null) {
                                    accumBalance = accumBalance.add(theStmt.aging30.amount);
                                    accumBalance = accumBalance.add(theStmt.aging60.amount);
                                    accumBalance = accumBalance.add(theStmt.aging90.amount);
                                    accumBalance = accumBalance.add(theStmt.aging120.amount);
                                    accumBalance = accumBalance.add(theStmt.aging150.amount);
                                    accumBalance = accumBalance.add(theStmt.aging1000.amount);
                                    Paragraph aging1 = new Paragraph("Aging Summary", tableFontBold);
                                    aging1.setAlignment(Element.ALIGN_LEFT);
                                    document.add(aging1);
                                    PdfPTable tAging = new PdfPTable(7);
                                    tAging.setWidthPercentage(100f);
                                    if (theStmt.AgingByPeriodType.equals(CustStmtOfAccountForm.AGING_BY_MONTH)) {
                                        Vector vecMonthName = new Vector();
                                        Timestamp tsCounter = theStmt.dateStart;
                                        SimpleDateFormat formatter = new SimpleDateFormat("MMM");
                                        long b;
                                        for (int i = 0; i < 5; i++) {
                                            b = tsCounter.getTime();
                                            java.util.Date bufDate = new java.util.Date(b);
                                            String strTimeStamp = formatter.format(bufDate);
                                            vecMonthName.add(strTimeStamp);
                                            tsCounter = TimeFormat.add(tsCounter, 0, -1, 0);
                                            tsCounter = TimeFormat.set(tsCounter, Calendar.DATE, 1);
                                        }
                                        tAging.addCell(makeCell((String) vecMonthName.get(0), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell((String) vecMonthName.get(1), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell((String) vecMonthName.get(2), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell((String) vecMonthName.get(3), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell((String) vecMonthName.get(4), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell(">5 MONTHS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("TOTAL", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                    } else {
                                        tAging.addCell(makeCell("30 DAYS", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("31-60 DAYS", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("61-90 DAYS", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("91-120 DAYS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("121-150 DAYS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("> 150 DAYS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                        tAging.addCell(makeCell("TOTAL", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                    }
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(theStmt.aging30.amount), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(theStmt.aging60.amount), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(theStmt.aging90.amount), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFontBold, leading, padding, withBorder, true, true));
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(theStmt.aging120.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(theStmt.aging150.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(theStmt.aging1000.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                    tAging.addCell(makeCell(CurrencyFormat.strCcy(accumBalance), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, withBorder, true, true));
                                    document.add(new Paragraph(" "));
                                    document.add(tAging);
                                }
                            }
                            Paragraph footer1 = new Paragraph("Please pay RM" + CurrencyFormat.strCcy(runningBal), tableFontBold);
                            footer1.setAlignment(Element.ALIGN_CENTER);
                            document.add(footer1);
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            document.newPage();
                        }
                    }
                    document.close();
                    outputStream.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if ("BATA".equals(AppConfigManager.getProperty("BATCH-PRINTING-OF-BILLING-STATEMENT-FORMAT-OPTION"))) {
            try {
                CusBillingStatmentPrintBatch cbspb = (CusBillingStatmentPrintBatch) session.getAttribute("cust-billing-stmt-batch");
                Vector vecCustId = cbspb.getCustId();
                if (vecCustId.size() > 0) {
                    TreeMap treeStmt = cbspb.treeStmt;
                    TreeMap treeAging = cbspb.treeAging;
                    res.setContentType("text/pdf");
                    res.setHeader("Content-disposition", "filename=billStmt.pdf");
                    BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
                    Document document = new Document();
                    PdfWriter.getInstance(document, outputStream);
                    document.open();
                    Font tableFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11);
                    Font tableFontBold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, Color.BLACK);
                    float padding = 2f;
                    float leading = 10f;
                    Rectangle noBorder = new Rectangle(0f, 0f);
                    noBorder.setBorderWidthLeft(0f);
                    noBorder.setBorderWidthBottom(0f);
                    noBorder.setBorderWidthRight(0f);
                    noBorder.setBorderWidthTop(0f);
                    Rectangle withBorder = new Rectangle(0f, 0f);
                    withBorder.setBorderWidthLeft(0f);
                    withBorder.setBorderWidthBottom(1f);
                    withBorder.setBorderWidthRight(0f);
                    withBorder.setBorderWidthTop(1f);
                    Rectangle withTopLeftBorder = new Rectangle(0f, 0f);
                    withTopLeftBorder.setBorderWidthLeft(1f);
                    withTopLeftBorder.setBorderWidthBottom(0f);
                    withTopLeftBorder.setBorderWidthRight(0f);
                    withTopLeftBorder.setBorderWidthTop(1f);
                    Rectangle withTopLeftRightBorder = new Rectangle(0f, 0f);
                    withTopLeftRightBorder.setBorderWidthLeft(1f);
                    withTopLeftRightBorder.setBorderWidthBottom(0f);
                    withTopLeftRightBorder.setBorderWidthRight(1f);
                    withTopLeftRightBorder.setBorderWidthTop(1f);
                    Rectangle withTopLeftBottomBorder = new Rectangle(0f, 0f);
                    withTopLeftBottomBorder.setBorderWidthLeft(1f);
                    withTopLeftBottomBorder.setBorderWidthBottom(1f);
                    withTopLeftBottomBorder.setBorderWidthRight(0f);
                    withTopLeftBottomBorder.setBorderWidthTop(1f);
                    Rectangle withFullBorder = new Rectangle(0f, 0f);
                    withFullBorder.setBorderWidthLeft(1f);
                    withFullBorder.setBorderWidthBottom(1f);
                    withFullBorder.setBorderWidthRight(1f);
                    withFullBorder.setBorderWidthTop(1f);
                    for (int cnt2 = 0; cnt2 < vecCustId.size(); cnt2++) {
                        Integer iCustPkid = (Integer) vecCustId.get(cnt2);
                        String custPkid = iCustPkid.toString();
                        System.out.println("iCustPkid : " + custPkid);
                        String month = cbspb.getMonth();
                        Timestamp tsCurrent = TimeFormat.getTimestamp();
                        CustBillingStmtObject cbsObj = (CustBillingStmtObject) treeStmt.get(iCustPkid);
                        if (cbsObj != null) {
                            int nRowsPerPage = 14;
                            int nDocRows = 0;
                            nDocRows = cbsObj.vecNominalAccountTxn.size();
                            int nTotalPages = (nDocRows / nRowsPerPage) + 1;
                            Timestamp tsMonth = TimeFormat.createTimestamp(month);
                            BigDecimal runningBal = new BigDecimal("0.00");
                            Integer branchId = cbspb.getBranch();
                            String branchDetails = "";
                            Image jpg = null;
                            if (branchId.intValue() > 0) {
                                BranchObject branch = BranchNut.getObject(cbspb.getBranch());
                                if (branch != null) {
                                    branchDetails = branch.name + " " + "(" + branch.regNo + ")";
                                    if (branch.addr1.length() > 1) branchDetails += "\n" + branch.addr1 + ",";
                                    if (branch.addr2.length() > 1) branchDetails += " " + branch.addr2;
                                    if (branch.addr3.length() > 1) branchDetails += "\n" + branch.addr3;
                                    if (branch.zip.length() > 1) branchDetails += " " + branch.zip;
                                    if (branch.state.length() > 1) branchDetails += "\n" + branch.state;
                                    if (branch.phoneNo.length() > 1) branchDetails += "\n\n\n" + "Tel:" + branch.phoneNo + "     ";
                                    if (branch.faxNo.length() > 1) branchDetails += "Fax:" + branch.faxNo;
                                }
                            }
                            String cHeader = "STATEMENT OF ACCOUNT";
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            float[] widthsBdetails = { 70f, 30f };
                            PdfPTable bDetails = new PdfPTable(widthsBdetails);
                            bDetails.setWidthPercentage(100f);
                            bDetails.getDefaultCell().setBorderColor(new Color(255, 255, 255));
                            bDetails.addCell(makeCell(branchDetails, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            bDetails.addCell(makeCell(cHeader, Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFontBold, leading, padding, noBorder, true, true));
                            document.add(bDetails);
                            Paragraph bUnderlines = new Paragraph("_______________________________________________________________________________________________", tableFont);
                            bUnderlines.setAlignment(Element.ALIGN_CENTER);
                            document.add(bUnderlines);
                            document.add(new Paragraph(" "));
                            String strDetails1 = cbsObj.mCustAccObj.name + " " + cbsObj.mCustAccObj.identityNumber;
                            if (cbsObj.mCustAccObj != null) {
                                System.out.println("eeeeee");
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainAddress1;
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainAddress2;
                                if (!cbsObj.mCustAccObj.mainAddress3.equals("")) {
                                    strDetails1 += "\n" + cbsObj.mCustAccObj.mainAddress3;
                                }
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainCity + " " + cbsObj.mCustAccObj.mainPostcode;
                                strDetails1 += "\n" + cbsObj.mCustAccObj.mainState + " " + cbsObj.mCustAccObj.mainCountry;
                            }
                            Timestamp dateStart = TimeFormat.set(tsMonth, Calendar.DATE, 1);
                            Timestamp dateEnd = TimeFormat.add(dateStart, 0, 1, 0);
                            dateEnd = TimeFormat.add(dateEnd, 0, 0, -1);
                            String strDetails2 = "For the month ended : " + TimeFormat.format(dateEnd, "yyyy-MM-dd");
                            strDetails2 += "\n" + "Customer Code         : " + cbsObj.mCustAccObj.pkid.toString();
                            strDetails2 += "\n" + "Sales Rep   	               : " + UserNut.getUserName(cbsObj.mCustAccObj.salesman);
                            strDetails2 += "\n" + "Terms   	                     : " + cbsObj.mCustAccObj.creditTerms.toString() + " days";
                            strDetails2 += "\n" + "Page   	                        : 1";
                            PdfPTable tDetails = new PdfPTable(new float[] { 60f, 40f });
                            tDetails.setWidthPercentage(100f);
                            tDetails.addCell(makeCell(strDetails1, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            tDetails.addCell(makeCell(strDetails2, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                            document.add(tDetails);
                            document.add(new Paragraph(" "));
                            for (int nCurrentPage = 0; nCurrentPage < nTotalPages; nCurrentPage++) {
                                if (nCurrentPage + 1 > 1) {
                                    document.add(new Paragraph(" "));
                                    document.add(new Paragraph(" "));
                                    document.add(bDetails);
                                    document.add(bUnderlines);
                                    document.add(new Paragraph(" "));
                                    String strDetails3 = "For the month ended : " + TimeFormat.format(dateEnd, "yyyy-MM-dd");
                                    strDetails3 += "\n" + "Customer Code         : " + cbsObj.mCustAccObj.pkid.toString();
                                    strDetails3 += "\n" + "Sales Rep   	               : " + UserNut.getUserName(cbsObj.mCustAccObj.salesman);
                                    strDetails3 += "\n" + "Terms   	                     : " + cbsObj.mCustAccObj.creditTerms.toString() + " days";
                                    strDetails3 += "\n" + "Page   	                        : " + (new Integer(nCurrentPage + 1)).toString();
                                    PdfPTable tDetails2 = new PdfPTable(new float[] { 60f, 40f });
                                    tDetails2.setWidthPercentage(100f);
                                    tDetails2.addCell(makeCell(strDetails1, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    tDetails2.addCell(makeCell(strDetails3, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    document.add(tDetails2);
                                    document.add(new Paragraph(" "));
                                }
                                float[] widths = { 14f, 12f, 38f, 12f, 12f, 12f };
                                PdfPTable tEntryHeader = new PdfPTable(widths);
                                tEntryHeader.setWidthPercentage(100f);
                                tEntryHeader.addCell(makeCellBgColor("Date", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withBorder, true, true, Color.LIGHT_GRAY));
                                tEntryHeader.addCell(makeCellBgColor("Ref No", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withBorder, true, true, Color.LIGHT_GRAY));
                                tEntryHeader.addCell(makeCellBgColor("Description", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withBorder, true, true, Color.LIGHT_GRAY));
                                tEntryHeader.addCell(makeCellBgColor("Debit", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withBorder, true, true, Color.LIGHT_GRAY));
                                tEntryHeader.addCell(makeCellBgColor("Credit", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withBorder, true, true, Color.LIGHT_GRAY));
                                tEntryHeader.addCell(makeCellBgColor("Balance", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withBorder, true, true, Color.LIGHT_GRAY));
                                document.add(tEntryHeader);
                                PdfPTable tEntries3 = new PdfPTable(widths);
                                tEntries3.setWidthPercentage(100f);
                                if (nCurrentPage + 1 == 1) {
                                    runningBal = runningBal.add(cbsObj.bdPrevBalance);
                                    tEntries3.addCell(makeCell(TimeFormat.strDisplayDate(cbsObj.mDateFrom), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    tEntries3.addCell(makeCell(" ", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    tEntries3.addCell(makeCell("Balance b/d", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    if (cbsObj.bdPrevBalance.signum() > 0) {
                                        tEntries3.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                        tEntries3.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    } else {
                                        tEntries3.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                        tEntries3.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    }
                                    tEntries3.addCell(makeCell(CurrencyFormat.comma(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    document.add(tEntries3);
                                }
                                PdfPTable tEntries = new PdfPTable(widths);
                                tEntries.setWidthPercentage(100f);
                                for (int cnt1 = 0; cnt1 < nRowsPerPage; cnt1++) {
                                    int rowIndex = (cnt1 % nRowsPerPage) + nCurrentPage * nRowsPerPage;
                                    if (rowIndex < cbsObj.vecNominalAccountTxn.size()) {
                                        NominalAccountTxnObject natObj = (NominalAccountTxnObject) cbsObj.vecNominalAccountTxn.get(rowIndex);
                                        Timestamp tsDate = TimeFormat.getTimestamp();
                                        if (natObj.timeOption1.equals(NominalAccountTxnBean.TIME_STMT)) {
                                            tsDate = natObj.timeParam1;
                                        }
                                        if (natObj.timeOption2.equals(NominalAccountTxnBean.TIME_STMT)) {
                                            tsDate = natObj.timeParam2;
                                        }
                                        runningBal = runningBal.add(natObj.amount);
                                        tEntries.addCell(makeCell(TimeFormat.strDisplayDate(tsDate), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        tEntries.addCell(makeCell(docPrefix(natObj.foreignTable, natObj.foreignKey.toString()), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        if (InvoiceBean.TABLENAME.equals(natObj.foreignTable)) {
                                            tEntries.addCell(makeCell("Sales", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        } else if (SalesReturnBean.TABLENAME.equals(natObj.foreignTable)) {
                                            tEntries.addCell(makeCell("Sales Return", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        } else if (CreditMemoIndexBean.TABLENAME.equals(natObj.foreignTable)) {
                                            tEntries.addCell(makeCell(natObj.description.toString(), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        } else if (OfficialReceiptBean.TABLENAME.equals(natObj.foreignTable)) {
                                            Vector vecDocLink = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, natObj.foreignKey));
                                            String tgtDocId = "";
                                            for (int cnt4 = 0; cnt4 < vecDocLink.size(); cnt4++) {
                                                DocLinkObject dlObj = (DocLinkObject) vecDocLink.get(cnt4);
                                                tgtDocId += "Inv" + dlObj.tgtDocId.toString() + "  ";
                                            }
                                            tEntries.addCell(makeCell("Payment " + tgtDocId, Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        } else {
                                            tEntries.addCell(makeCell(" ", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                        }
                                        if (OfficialReceiptBean.TABLENAME.equals(natObj.foreignTable)) {
                                            Vector vecDocLink = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, natObj.foreignKey));
                                            if (vecDocLink.size() > 1) {
                                                if (natObj.amount.signum() > 0) {
                                                    tEntries.addCell(makeCell(CurrencyFormat.strCcy(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                    tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                } else {
                                                    tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                    tEntries.addCell(makeCell(CurrencyFormat.strCcy(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                }
                                                tEntries.addCell(makeCell(CurrencyFormat.comma(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                            } else {
                                                if (natObj.amount.signum() > 0) {
                                                    tEntries.addCell(makeCell(CurrencyFormat.strCcy(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                    tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                } else {
                                                    tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                    tEntries.addCell(makeCell(CurrencyFormat.comma(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                }
                                                tEntries.addCell(makeCell(CurrencyFormat.comma(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                            }
                                        } else {
                                            if (natObj.amount.signum() > 0) {
                                                tEntries.addCell(makeCell(CurrencyFormat.strCcy(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                            } else {
                                                tEntries.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                                tEntries.addCell(makeCell(CurrencyFormat.comma(natObj.amount.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                            }
                                            tEntries.addCell(makeCell(CurrencyFormat.comma(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                        }
                                    }
                                }
                                document.add(tEntries);
                                if (nCurrentPage + 1 == nTotalPages) {
                                    float[] widths2 = { 14f, 12f, 38f, 12f, 12f, 12f };
                                    PdfPTable tEntries2 = new PdfPTable(widths2);
                                    tEntries2.setWidthPercentage(100f);
                                    tEntries2.addCell(makeCell(TimeFormat.strDisplayDate(cbsObj.mDateTo), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    tEntries2.addCell(makeCell(" ", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    tEntries2.addCell(makeCell("Total Outstanding (O/S)", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, noBorder, true, true));
                                    if (runningBal.signum() > 0) {
                                        tEntries2.addCell(makeCell(CurrencyFormat.comma(runningBal.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                        tEntries2.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    } else {
                                        tEntries2.addCell(makeCell("   ", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                        tEntries2.addCell(makeCell(CurrencyFormat.comma(runningBal.abs()), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    }
                                    tEntries2.addCell(makeCell(CurrencyFormat.comma(runningBal), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, noBorder, true, true));
                                    document.add(tEntries2);
                                    document.add(new Paragraph(" "));
                                    document.add(new Paragraph(" "));
                                    document.add(bUnderlines);
                                } else {
                                    document.add(bUnderlines);
                                    Paragraph continueText = new Paragraph("....continue");
                                    continueText.setAlignment(Element.ALIGN_RIGHT);
                                    document.add(continueText);
                                }
                                if (nCurrentPage + 1 == nTotalPages) {
                                    CustStmtOfAccountForm stmtForm = (CustStmtOfAccountForm) treeAging.get(iCustPkid);
                                    ;
                                    BigDecimal accumBalance = new BigDecimal(0);
                                    if (stmtForm != null) {
                                        CustStmtOfAccountForm.StmtOfAcc theStmt = stmtForm.getStmtReport();
                                        if (theStmt != null) {
                                            accumBalance = accumBalance.add(theStmt.aging30.amount);
                                            accumBalance = accumBalance.add(theStmt.aging60.amount);
                                            accumBalance = accumBalance.add(theStmt.aging90.amount);
                                            accumBalance = accumBalance.add(theStmt.aging120.amount);
                                            accumBalance = accumBalance.add(theStmt.aging150.amount);
                                            accumBalance = accumBalance.add(theStmt.aging1000.amount);
                                            Paragraph aging1 = new Paragraph("Aging Summary", tableFontBold);
                                            aging1.setAlignment(Element.ALIGN_LEFT);
                                            document.add(aging1);
                                            float[] widths4 = { 12f, 14f, 14f, 16f, 17f, 14f, 13f };
                                            PdfPTable tAging = new PdfPTable(widths4);
                                            tAging.setWidthPercentage(100f);
                                            if (theStmt.AgingByPeriodType.equals(CustStmtOfAccountForm.AGING_BY_MONTH)) {
                                                Vector vecMonthName = new Vector();
                                                Timestamp tsCounter = theStmt.dateStart;
                                                SimpleDateFormat formatter = new SimpleDateFormat("MMM");
                                                long b;
                                                for (int i = 0; i < 5; i++) {
                                                    b = tsCounter.getTime();
                                                    java.util.Date bufDate = new java.util.Date(b);
                                                    String strTimeStamp = formatter.format(bufDate);
                                                    vecMonthName.add(strTimeStamp);
                                                    tsCounter = TimeFormat.add(tsCounter, 0, -1, 0);
                                                    tsCounter = TimeFormat.set(tsCounter, Calendar.DATE, 1);
                                                }
                                                tAging.addCell(makeCell((String) vecMonthName.get(0), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell((String) vecMonthName.get(1), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell((String) vecMonthName.get(2), Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell((String) vecMonthName.get(3), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell((String) vecMonthName.get(4), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell(">5 MONTHS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell("TOTAL O/S", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftRightBorder, true, true));
                                            } else {
                                                tAging.addCell(makeCell("<30 DAYS", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell("31-60 DAYS", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell("61-90 DAYS", Element.ALIGN_TOP, Element.ALIGN_LEFT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell("91-120 DAYS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell("121-150 DAYS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell(">150 DAYS", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBorder, true, true));
                                                tAging.addCell(makeCell("TOTAL O/S", Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftRightBorder, true, true));
                                            }
                                            tAging.addCell(makeCell(CurrencyFormat.comma(theStmt.aging30.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBottomBorder, true, true));
                                            tAging.addCell(makeCell(CurrencyFormat.comma(theStmt.aging60.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBottomBorder, true, true));
                                            tAging.addCell(makeCell(CurrencyFormat.comma(theStmt.aging90.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBottomBorder, true, true));
                                            tAging.addCell(makeCell(CurrencyFormat.comma(theStmt.aging120.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBottomBorder, true, true));
                                            tAging.addCell(makeCell(CurrencyFormat.comma(theStmt.aging150.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBottomBorder, true, true));
                                            tAging.addCell(makeCell(CurrencyFormat.comma(theStmt.aging1000.amount), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withTopLeftBottomBorder, true, true));
                                            tAging.addCell(makeCell(CurrencyFormat.comma(accumBalance), Element.ALIGN_TOP, Element.ALIGN_RIGHT, tableFont, leading, padding, withFullBorder, true, true));
                                            document.add(new Paragraph(" "));
                                            document.add(tAging);
                                        }
                                    }
                                    Paragraph footer = new Paragraph("Note: Payment received after the end of month will be entered in next month's statement." + "\n      If no remarks are made within 7 days, account will be considered as correct.", tableFont);
                                    footer.setAlignment(Element.ALIGN_CENTER);
                                    document.add(footer);
                                    Paragraph footer2 = new Paragraph(AppConfigManager.getProperty("BATCH-PRINTING-OF-BILLING-STATEMENT-FOOTER"));
                                    footer2.setAlignment(Element.ALIGN_CENTER);
                                    document.add(footer2);
                                }
                                document.newPage();
                            }
                        }
                    }
                    document.close();
                    outputStream.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
