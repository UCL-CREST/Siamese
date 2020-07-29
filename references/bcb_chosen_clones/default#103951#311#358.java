    public void ReadSeriesFile() {
        xmlSeriesInfo = new ArrayList<XMLShowInfo>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("JavaNZB.series"));
            doc.getDocumentElement().normalize();
            NodeList listOfShowInfo = doc.getElementsByTagName("Show");
            ArrayList<String> elementNames = new ArrayList<String>();
            elementNames.add("Name");
            elementNames.add("SearchBy");
            elementNames.add("Episode");
            elementNames.add("Format");
            elementNames.add("Language");
            elementNames.add("Next");
            elementNames.add("Season");
            for (int s = 0; s < listOfShowInfo.getLength(); s++) {
                Node showNode = listOfShowInfo.item(s);
                XMLShowInfo xmlShowInfo = new XMLShowInfo();
                if (showNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element firstPersonElement = (Element) showNode;
                    for (String CurElementName : elementNames) {
                        NodeList NameList = firstPersonElement.getElementsByTagName(CurElementName);
                        Element NameElement = (Element) NameList.item(0);
                        NodeList textFNList = NameElement.getChildNodes();
                        if (textFNList.getLength() > 0) {
                            if (CurElementName.equals("Name")) xmlShowInfo.showName = ((Node) textFNList.item(0)).getNodeValue().trim();
                            if (CurElementName.equals("SearchBy")) xmlShowInfo.searchBy = ((Node) textFNList.item(0)).getNodeValue().trim();
                            if (CurElementName.equals("Episode")) xmlShowInfo.episode = ((Node) textFNList.item(0)).getNodeValue().trim();
                            if (CurElementName.equals("Format")) xmlShowInfo.format = ((Node) textFNList.item(0)).getNodeValue().trim();
                            if (CurElementName.equals("Language")) xmlShowInfo.language = ((Node) textFNList.item(0)).getNodeValue().trim();
                            if (CurElementName.equals("Next")) xmlShowInfo.next = ((Node) textFNList.item(0)).getNodeValue().trim();
                            if (CurElementName.equals("Season")) xmlShowInfo.season = ((Node) textFNList.item(0)).getNodeValue().trim();
                        }
                    }
                    xmlSeriesInfo.add(xmlShowInfo);
                }
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
