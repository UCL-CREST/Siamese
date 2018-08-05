    private byte[] getTunerList(HTTPurl urlData) throws Exception {
        boolean showID = "true".equalsIgnoreCase(urlData.getParameter("showid"));
        showID = showID | "true".equalsIgnoreCase(urlData.getCookie("showDeviceID"));
        if ("false".equalsIgnoreCase(urlData.getParameter("showid"))) showID = false;
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "CardSetup.html");
        if (showID == true) template.addCookie("showDeviceID", "true"); else template.addCookie("showDeviceID", "false");
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        int activeDevices = devList.getActiveDeviceCount();
        boolean testMode = "1".equals(store.getProperty("tools.testmode"));
        String scanCommand = "";
        if (testMode == true) {
            scanCommand = "win32/device.exe -test";
        } else {
            scanCommand = "win32/device.exe";
        }
        System.out.println("Running device scan command: " + scanCommand);
        Runtime runner = Runtime.getRuntime();
        Process scan = runner.exec(scanCommand);
        TunerScanResult tuners = new TunerScanResult();
        tuners.readInput(scan.getInputStream());
        tuners.parseXML();
        StringBuffer out = new StringBuffer();
        Vector<CaptureDevice> tunersList = tuners.getResult();
        out.append("<tr><td colspan='3' style='border: 1px solid #FFFFFF;'>");
        out.append("<table width='100%' border='0' cellpadding='0' cellspacing='0'><tr><td><strong>Currently Selected Devices</strong></td><td align='right'>");
        if (showID) out.append("<a style='text-decoration: none; color: #FFFFFF; font-size: 12px;' href='/servlet/SystemDataRes?action=04&showid=false'>Hide IDs</a>"); else out.append("<a style='text-decoration: none; color: #FFFFFF; font-size: 12px;' href='/servlet/SystemDataRes?action=04&showid=true'>Show IDs</a>");
        out.append("</td></tr></table></td></tr>\n");
        for (int x = 0; x < devList.getDeviceCount(); x++) {
            CaptureDevice cd = (CaptureDevice) devList.getDevice(x);
            out.append("<tr>");
            out.append("<td nowrap>" + x + "</td>");
            out.append("<td nowrap>: ");
            out.append(cd.getName());
            if (cd.isInUse() == true) out.append(" (Active)");
            boolean isAvailable = false;
            for (int y = 0; y < tunersList.size(); y++) {
                CaptureDevice cd2 = (CaptureDevice) tunersList.get(y);
                if (cd.getID().equals(cd2.getID())) {
                    isAvailable = true;
                    break;
                }
            }
            if (isAvailable == false) out.append(" <img border='0' alt='Not Available' title='Device Not Available' src='/images/exclaim24.png' align='absmiddle' width='22' height='24'> ");
            if (showID) out.append("(" + cd.getID() + ")");
            out.append("</td>\n");
            out.append("<td nowrap width='50px'> ");
            out.append(" <a href='/servlet/SystemDataRes?action=14&tunerID=" + x + "'><img border='0' alt='DEL' src='/images/delete.png' align='absmiddle' width='24' height='24'></a> ");
            out.append("<a href='/servlet/SystemDataRes?action=15&tunerID=" + x + "'><img border='0' alt='Up' src='/images/up01.png' align='absmiddle' width='7' height='7'></a> ");
            out.append("<a href='/servlet/SystemDataRes?action=16&tunerID=" + x + "'><img border='0' alt='Down' src='/images/down01.png' align='absmiddle' width='7' height='7'></a>");
            out.append("</td>\n");
            out.append("</tr>\n");
        }
        if (devList.getDeviceCount() == 0) {
            out.append("<tr><td colspan ='3'>No devices selected</td></tr>");
        }
        int numCards = 0;
        out.append("<tr><td colspan='3'><strong>&nbsp;</strong></td></tr>");
        out.append("<tr><td colspan='3' style='border: 1px solid #FFFFFF;'><strong>Devices Available But Not Selected</strong></td></tr>");
        for (int x = 0; x < tunersList.size(); x++) {
            CaptureDevice dev = (CaptureDevice) tunersList.get(x);
            boolean found = false;
            for (int y = 0; y < devList.getDeviceCount(); y++) {
                CaptureDevice cd = (CaptureDevice) devList.getDevice(y);
                if (cd.getID().equals(dev.getID())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                out.append("<tr>");
                out.append("<td>&nbsp;</td>");
                out.append("<td nowrap>" + dev.getName() + "</td>");
                out.append("<td width='50px'><a href='/servlet/SystemDataRes?action=13&tunerID=" + URLEncoder.encode(dev.getID(), "UTF-8"));
                out.append("&tunerName=" + URLEncoder.encode(dev.getName(), "UTF-8") + "'>");
                out.append("<img border='0' alt='ADD' src='/images/add.png' align='absmiddle' width='24' height='24'></a></td>\n");
                out.append("</tr>\n");
                numCards++;
            }
        }
        if (numCards == 0) {
            out.append("<tr><td colspan ='3'>No devices available</td></tr>");
        }
        numCards = 0;
        template.replaceAll("$cardList", out.toString());
        template.replaceAll("$cardCount", new Integer(activeDevices).toString());
        return template.getPageBytes();
    }
