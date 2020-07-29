    public int parseXML() throws Exception {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream reader = new ByteArrayInputStream(xmlData.toString().getBytes());
            Document doc = docBuilder.parse(reader);
            NodeList devices = doc.getElementsByTagName("item");
            for (int x = 0; x < devices.getLength(); x++) {
                Node device = devices.item(x);
                NodeList nl = device.getChildNodes();
                String name = "";
                String id = "";
                for (int y = 0; y < nl.getLength(); y++) {
                    Node item = nl.item(y);
                    if (item.getNodeName().equals("name")) {
                        name = item.getTextContent();
                    } else if (item.getNodeName().equals("id")) {
                        id = item.getTextContent();
                    }
                }
                if (name.length() > 0 && id.length() > 0) {
                    CaptureDevice dev = new CaptureDevice(name, id);
                    scanResult.add(dev);
                }
            }
            return scanResult.size();
        } catch (Exception e) {
            System.out.println("Error parsing device XML.");
            System.out.println(xmlData);
            scanResult = new Vector<CaptureDevice>();
            return 0;
        }
    }
