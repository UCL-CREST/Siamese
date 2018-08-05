    public ProductListByCatHandler(int cathegory, int langId) {
        try {
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy?method=GetProductListByCategory&category_id=" + cathegory + "&language_id=" + langId);
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(false);
            urlc.setAllowUserInteraction(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            br.close();
            String response = sb.toString();
            if (response == null) {
                return;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(response));
            Document dom = db.parse(is);
            NodeList nl = dom.getElementsByTagName("product");
            for (int i = 0; i < nl.getLength(); i++) {
                Element nodes = (Element) nl.item(i);
                String id = nodes.getAttribute("id").toString();
                NodeList name = nodes.getElementsByTagName("name");
                NodeList rank2 = nodes.getElementsByTagName("sales_rank");
                NodeList price = nodes.getElementsByTagName("price");
                NodeList url2 = nodes.getElementsByTagName("image_url");
                String nameS = getCharacterDataFromElement((Element) name.item(0));
                String rank2S = getCharacterDataFromElement((Element) rank2.item(0));
                String priceS = getCharacterDataFromElement((Element) price.item(0));
                String url2S = getCharacterDataFromElement((Element) url2.item(0));
                list.add(new ProductShort(id, nameS, rank2S, priceS, url2S));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
