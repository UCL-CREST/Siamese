    public String getWeather(String cityName, String fileAddr) {
        try {
            URL url = new URL("http://www.google.com/ig/api?hl=zh_cn&weather=" + cityName);
            InputStream inputstream = url.openStream();
            String s, str;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputstream));
            StringBuffer stringbuffer = new StringBuffer();
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAddr), "utf-8"));
            while ((s = in.readLine()) != null) {
                stringbuffer.append(s);
            }
            str = new String(stringbuffer);
            out.write(str);
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(fileAddr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String str = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList nodelist1 = (NodeList) doc.getElementsByTagName("forecast_conditions");
            NodeList nodelist2 = nodelist1.item(0).getChildNodes();
            str = nodelist2.item(4).getAttributes().item(0).getNodeValue() + ",temperature:" + nodelist2.item(1).getAttributes().item(0).getNodeValue() + "℃-" + nodelist2.item(2).getAttributes().item(0).getNodeValue() + "℃";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
