    public static Collection<Tuple<String, ArrayList<String>>> readGeoParserResult(String recordContent, boolean getGazeteerIds) {
        if (TESTING) {
            HashSet<Tuple<String, ArrayList<String>>> ret = new HashSet<Tuple<String, ArrayList<String>>>();
            ret.add(new Tuple<String, ArrayList<String>>("teste", new ArrayList<String>()));
            return ret;
        }
        int retries = 0;
        while (retries < 3) {
            try {
                ArrayList<Tuple<String, ArrayList<String>>> ret = new ArrayList<Tuple<String, ArrayList<String>>>();
                String reqPre = "<?xml version=\"1.0\"?>\r\n" + (getGazeteerIds ? "<GetFeature" : "<GetParsing") + " xmlns=\"http://www.opengis.net/gp\" xmlns:wfs=\"http://www.opengis.net/wfs\"" + " xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\"" + " xsi:schemaLocation=\"http://www.opengis.net/gp ../gp/GetFeatureRequest.xsd http://www.opengis.net/wfs ../wfs/GetFeatureRequest.xsd\"\r\n" + " wfs:outputFormat=\"GML2\">" + "<wfs:Query wfs:TypeName=\"PlaceName\" />" + "<Resource mine=\"text/plain\">" + "<Contents></Contents>" + "</Resource>" + (getGazeteerIds ? "</GetFeature>" : "</GetParsing>");
                Document doc = DocumentHelper.parseText(reqPre);
                doc.getRootElement().element("Resource").element("Contents").setText(recordContent);
                URL url = new URL(geoParserBaseUrl + "?request=" + URLEncoder.encode(doc.asXML(), "ISO8859-1"));
                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
                BufferedReader buffered = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = buffered.readLine()) != null) {
                    sb.append(line);
                }
                Document d = DocumentHelper.parseText(sb.toString());
                HashSet<String> places = new HashSet<String>();
                for (Iterator<Element> it = d.getRootElement().element("EntryCollection").elementIterator("PlaceName"); it.hasNext(); ) {
                    Element plcEl = it.next();
                    String val = plcEl.elementTextTrim("TermName");
                    if (!val.equals("") && !places.contains(val)) {
                        places.add(val);
                        String entryID = plcEl.attributeValue("entryID");
                        Tuple<String, ArrayList<String>> plc = new Tuple<String, ArrayList<String>>(val, new ArrayList<String>());
                        for (Iterator<Element> it2 = d.getRootElement().element("EntryCollection").elementIterator("GazetteerEntry"); it2.hasNext(); ) {
                            Element idEl = it2.next();
                            if (idEl.attributeValue("entryID").equals(entryID)) plc.getV2().add(idEl.attributeValue("id"));
                        }
                        ret.add(plc);
                    }
                }
                return ret;
            } catch (Exception e) {
                log.debug("Erro ao pesquisar a lista de termos para o registo " + recordContent + "! " + e.getMessage(), e);
                System.out.println("Erro ao pesquisar a lista de termos para o registo " + recordContent + "! " + e.getMessage());
                retries++;
            }
        }
        System.out.println("Too many retries. Giving up.");
        return new HashSet<Tuple<String, ArrayList<String>>>();
    }
