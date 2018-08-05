    public void createCCDSection() {
        int numProblems = mCCRProblems.getLength();
        if (numProblems == 0) return;
        mCCDProblemsRoot.appendChild(mCDA.createComment("******************************"));
        mCCDProblemsRoot.appendChild(mCDA.createComment("       Problems Section       "));
        mCCDProblemsRoot.appendChild(mCDA.createComment("******************************"));
        Element el = mCDA.createElement("component");
        mCCDProblemsRoot.appendChild(el);
        Element el2 = mCDA.createElement("section");
        el.appendChild(el2);
        Element el3 = mCDA.createElement("templateId");
        el3.setAttribute("root", "2.16.840.1.113883.10.20.1.11");
        el2.appendChild(el3);
        el3 = mCDA.createElement("code");
        el3.setAttribute("code", "11450-4");
        el3.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        el3.setAttribute("codeSystemName", "LOINC");
        el2.appendChild(el3);
        el3 = mCDA.createElement("title");
        el3.setTextContent("Problems");
        el2.appendChild(el3);
        el3 = mCDA.createElement("text");
        el2.appendChild(el3);
        Element el4 = mCDA.createElement("table");
        el4.setAttribute("border", "1");
        el4.setAttribute("width", "100%");
        el3.appendChild(el4);
        Element el5 = mCDA.createElement("thead");
        el4.appendChild(el5);
        Element el6 = mCDA.createElement("tr");
        el5.appendChild(el6);
        Element el7 = mCDA.createElement("th");
        el7.setTextContent("Condition");
        el6.appendChild(el7);
        el7 = mCDA.createElement("th");
        el7.setTextContent("Effective Dates");
        el6.appendChild(el7);
        el7 = mCDA.createElement("th");
        el7.setTextContent("Condition Status");
        el6.appendChild(el7);
        el5 = mCDA.createElement("tbody");
        el4.appendChild(el5);
        XPathExpression expression = null;
        try {
            for (int i = 0; i < numProblems; i++) {
                org.w3c.dom.Node node = mCCRProblems.item(i);
                el6 = mCDA.createElement("tr");
                el5.appendChild(el6);
                el7 = mCDA.createElement("td");
                expression = mXPath.compile("./ns0:Description/ns0:Text");
                el7.setTextContent((String) expression.evaluate(node, XPathConstants.STRING));
                el6.appendChild(el7);
                try {
                    expression = mXPath.compile("./ns0:DateTime/ns0:DateTimeRange/ns0:BeginRange/ns0:ExactDateTime");
                    String dt = (String) expression.evaluate(node, XPathConstants.STRING);
                    SimpleDateFormat sdf_in = new SimpleDateFormat("yyyy-MM-dd");
                    Date probDt = sdf_in.parse(dt);
                    SimpleDateFormat sdf_out = new SimpleDateFormat("MMM yyyy");
                    el7 = mCDA.createElement("td");
                    el7.setTextContent(sdf_out.format(Long.valueOf(probDt.getTime())));
                    el6.appendChild(el7);
                } catch (Exception exception) {
                }
                String status = "Active";
                expression = mXPath.compile("./ns0:DateTime/ns0:DateTimeRange/ns0:EndRange/ns0:ExactDateTime");
                String dt = (String) expression.evaluate(node, XPathConstants.STRING);
                if (dt != null && !dt.equals("")) status = "Resolved";
                el7 = mCDA.createElement("td");
                el7.setTextContent(status);
                el6.appendChild(el7);
            }
            for (int i = 0; i < numProblems; i++) {
                org.w3c.dom.Node node = mCCRProblems.item(i);
                el3 = mCDA.createElement("entry");
                el3.setAttribute("typeCode", "DRIV");
                el2.appendChild(el3);
                el4 = mCDA.createElement("act");
                el4.setAttribute("classCode", "ACT");
                el4.setAttribute("moodCode", "EVN");
                el3.appendChild(el4);
                el5 = mCDA.createElement("templateId");
                el5.setAttribute("root", "2.16.840.1.113883.10.20.1.27");
                el4.appendChild(el5);
                el5 = mCDA.createElement("id");
                el5.setAttribute("root", UUID.randomUUID().toString());
                el4.appendChild(el5);
                el5 = mCDA.createElement("code");
                el5.setAttribute("nullFlavor", "NA");
                el4.appendChild(el5);
                el5 = mCDA.createElement("entryRelationship");
                el5.setAttribute("typeCode", "SUBJ");
                el4.appendChild(el5);
                el6 = mCDA.createElement("observation");
                el6.setAttribute("classCode", "OBS");
                el6.setAttribute("moodCode", "EVN");
                el5.appendChild(el6);
                el7 = mCDA.createElement("templateId");
                el7.setAttribute("root", "2.16.840.1.113883.10.20.1.28");
                el6.appendChild(el7);
                el7 = mCDA.createElement("id");
                el7.setAttribute("root", UUID.randomUUID().toString());
                el6.appendChild(el7);
                el7 = mCDA.createElement("code");
                el7.setAttribute("code", "ASSERTION");
                el7.setAttribute("codeSystem", "2.16.840.1.113883.5.4");
                el7.setAttribute("codeSystemName", "ActCode");
                el6.appendChild(el7);
                el7 = mCDA.createElement("statusCode");
                el7.setAttribute("code", "completed");
                el6.appendChild(el7);
                try {
                    expression = mXPath.compile("./ns0:DateTime/ns0:DateTimeRange/ns0:BeginRange/ns0:ExactDateTime");
                    String dt = (String) expression.evaluate(node, XPathConstants.STRING);
                    SimpleDateFormat sdf_in = new SimpleDateFormat("yyyy-MM-dd");
                    Date procDt = sdf_in.parse(dt);
                    SimpleDateFormat sdf_out = new SimpleDateFormat("yyyyMM");
                    el7 = mCDA.createElement("effectiveTime");
                    Element el8 = mCDA.createElement("low");
                    el8.setAttribute("value", sdf_out.format(Long.valueOf(procDt.getTime())));
                    el7.appendChild(el8);
                    el6.appendChild(el7);
                } catch (Exception exception1) {
                }
                el7 = mCDA.createElement("value");
                expression = mXPath.compile("./ns0:Description/ns0:Code[ns0:CodingSystem='SNOMEDCT']/ns0:Value");
                String cd = (String) expression.evaluate(node, XPathConstants.STRING);
                if (!cd.equals("")) el7.setAttribute("code", cd);
                el7.setAttribute("xsi:type", "CD");
                el7.setAttribute("codeSystem", "2.16.840.1.113883.6.96");
                expression = mXPath.compile("./ns0:Description/ns0:Text");
                el7.setAttribute("displayName", (String) expression.evaluate(node, XPathConstants.STRING));
                el6.appendChild(el7);
                el7 = mCDA.createElement("entryRelationship");
                el7.setAttribute("typeCode", "REFR");
                el6.appendChild(el7);
                Element el8 = mCDA.createElement("observation");
                el8.setAttribute("classCode", "OBS");
                el8.setAttribute("moodCode", "EVN");
                el7.appendChild(el8);
                Element el9 = mCDA.createElement("templateId");
                el9.setAttribute("root", "2.16.840.1.113883.10.20.1.50");
                el8.appendChild(el9);
                el9 = mCDA.createElement("code");
                el9.setAttribute("code", "33999-4");
                el9.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
                el9.setAttribute("codeSystemName", "LOINC");
                el9.setAttribute("displayName", "Status");
                el8.appendChild(el9);
                el9 = mCDA.createElement("statusCode");
                el9.setAttribute("code", "completed");
                el8.appendChild(el9);
                el9 = mCDA.createElement("value");
                el9.setAttribute("xsi:type", "CE");
                el9.setAttribute("codeSystem", "2.16.840.1.113883.6.96");
                String code = "55561003";
                String displayName = "Active";
                expression = mXPath.compile("./ns0:DateTime/ns0:DateTimeRange/ns0:EndRange/ns0:ExactDateTime");
                String dt = (String) expression.evaluate(node, XPathConstants.STRING);
                if (dt != null && !dt.equals("")) {
                    displayName = "Resolved";
                    code = "413322009";
                }
                el9.setAttribute("code", code);
                el9.setAttribute("displayName", displayName);
                el8.appendChild(el9);
            }
        } catch (XPathExpressionException exp) {
            exp.printStackTrace();
        }
    }
