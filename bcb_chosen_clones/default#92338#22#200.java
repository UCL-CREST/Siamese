    public static void main(String[] args) {
        System.out.println("版本 1.2");
        if (args.length < 5) {
            System.out.println("Usage: java class <game_user> <phpsessid> <server> <x> <y> [min]");
            return;
        }
        String game_user = args[0];
        String PHPSESSID = args[1];
        String server = args[2];
        int start_x = Integer.parseInt(args[3]);
        int start_y = Integer.parseInt(args[4]);
        int count = 0;
        String[] paths = new String[10];
        int index_x_start = 0;
        int index_y_start = 0;
        int index_x_end = 700;
        int index_y_end = 700;
        if (args.length == 6) {
            if ("min".equalsIgnoreCase(args[5])) {
                index_y_start = 23;
                index_x_start = 60;
                index_y_end = 638;
                index_x_end = 634;
            }
        }
        for (int y = index_y_start; y < index_y_end; y++) {
            for (int x = index_x_start; x < index_x_end; x++) {
                if (y == index_y_start && x == index_x_start) {
                    x = start_x;
                    y = start_y;
                }
                try {
                    System.out.println("坐标 : " + x + "|" + y);
                    HttpURLConnection connect = (HttpURLConnection) new URL("http://" + server + ".17sanguo.com/index.php?act=map.detail&uitx=" + x + "&uity=" + y + "&keep=right&rand=459319").openConnection();
                    connect.setReadTimeout(1200000);
                    connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.17) Gecko/20080829 Firefox/2.0.0.17");
                    connect.setRequestProperty("cookie", game_user + "; PHPSESSID=" + PHPSESSID + "; __utmaen=1");
                    connect.connect();
                    InputStream inputStream = (InputStream) connect.getContent();
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = builder.parse(inputStream);
                    String text = "";
                    XPathFactory pathFactory = XPathFactory.newInstance();
                    XPath xpath = pathFactory.newXPath();
                    NodeList htmlList = (NodeList) xpath.evaluate("//html[@id='floatblockright']", doc, XPathConstants.NODESET);
                    for (int i = 0; i < htmlList.getLength(); i++) {
                        text = text + htmlList.item(i).getTextContent();
                    }
                    HTMLConfiguration config = new HTMLConfiguration();
                    config.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
                    DOMParser parser = new DOMParser(config);
                    StringReader sr = new StringReader(text);
                    parser.parse(new InputSource(sr));
                    Document doc1 = parser.getDocument();
                    NodeList infoList = (NodeList) xpath.evaluate("//div/h2/span[2]", doc1, XPathConstants.NODESET);
                    int index = infoList.item(0).getTextContent().trim().lastIndexOf("(");
                    String typeName = "未知";
                    if (index != -1) {
                        typeName = gUtil.filterString(infoList.item(0).getTextContent().trim().substring(0, index));
                    }
                    String x_coord = x + "";
                    String y_coord = y + "";
                    if (index != -1) {
                        String[] coords = infoList.item(0).getTextContent().trim().substring(index).replaceAll("[()]", "").split("\\|");
                        x_coord = coords[0].trim();
                        y_coord = coords[1].trim();
                    }
                    infoList = (NodeList) xpath.evaluate("//div", doc1, XPathConstants.NODESET);
                    int wood_add = 0;
                    int stone_add = 0;
                    int iron_add = 0;
                    int farm_add = 0;
                    if ("绿洲".equals(typeName)) {
                        NodeList greenList = (NodeList) xpath.evaluate("//div/img/@src", doc1, XPathConstants.NODESET);
                        String gifUrl = greenList.item(0).getNodeValue();
                        if (gifUrl.indexOf("map-wood-1") != -1) {
                            wood_add = 25;
                        } else if (gifUrl.indexOf("map-stone-1") != -1) {
                            stone_add = 25;
                        } else if (gifUrl.indexOf("map-tie-1") != -1) {
                            iron_add = 25;
                        } else if (gifUrl.indexOf("map-food-1") != -1) {
                            farm_add = 25;
                        } else if (gifUrl.indexOf("map-lumbercrop-1") != -1) {
                            wood_add = 25;
                            farm_add = 25;
                        } else if (gifUrl.indexOf("map-claycrop-1") != -1) {
                            stone_add = 25;
                            farm_add = 25;
                        } else if (gifUrl.indexOf("map-ironcrop-1") != -1) {
                            iron_add = 25;
                            farm_add = 25;
                        } else if (gifUrl.indexOf("map-crop-1") != -1) {
                            farm_add = 50;
                        }
                    }
                    String power = "";
                    String temp = infoList.item(0).getTextContent().trim();
                    if (temp.indexOf("势　力") != -1) {
                        if (temp.indexOf("蜀") != -1) {
                            power = "蜀";
                        } else if (temp.indexOf("魏") != -1) {
                            power = "魏";
                        } else {
                            power = "吴";
                        }
                    }
                    String king = "";
                    index = temp.indexOf("君　主：");
                    if (index != -1) {
                        king = gUtil.filterString(temp.substring(index + "君　主：".length(), temp.indexOf("\n", index)).trim());
                    }
                    String alliance = "";
                    index = temp.indexOf("联　盟：");
                    if (index != -1) {
                        alliance = gUtil.filterString(temp.substring(index + "联　盟：".length(), temp.indexOf("\n", index)).trim());
                    }
                    String population = "";
                    index = temp.indexOf("居　民：");
                    if (index != -1) {
                        population = gUtil.filterString(temp.substring(index + "居　民：".length(), temp.indexOf("\n", index)).trim());
                    }
                    String wood = "";
                    index = temp.indexOf("林　场：");
                    if (index != -1) {
                        wood = gUtil.filterString(temp.substring(index + "林　场：".length(), temp.indexOf("\n", index)).trim());
                    }
                    String stone = "";
                    index = temp.indexOf("采石场：");
                    if (index != -1) {
                        stone = gUtil.filterString(temp.substring(index + "采石场：".length(), temp.indexOf("\n", index)).trim());
                    }
                    String iron = "";
                    index = temp.indexOf("铁　矿：");
                    if (index != -1) {
                        iron = gUtil.filterString(temp.substring(index + "铁　矿：".length(), temp.indexOf("\n", index)).trim());
                    }
                    String farm = "";
                    index = temp.indexOf("农　田：");
                    if (index != -1) {
                        farm = gUtil.filterString(temp.substring(index + "农　田：".length(), temp.indexOf("\n", index)).trim());
                    }
                    System.out.println("[typeName=" + typeName + "][x=" + x_coord + "][y=" + y_coord + "][power=" + power + "][king=" + king + "][alliance=" + alliance + "][population=" + population + "][wood_add=" + wood_add + "][stone_add=" + stone_add + "][iron_add=" + iron_add + "][farm_add=" + farm_add + "][wood=" + wood + "][stone=" + stone + "][iron=" + iron + "][farm=" + farm + "]");
                    paths[count] = "";
                    paths[count] = paths[count] + "server" + count + "=" + server;
                    paths[count] = paths[count] + "&town_x" + count + "=" + x_coord;
                    paths[count] = paths[count] + "&town_y" + count + "=" + y_coord;
                    paths[count] = paths[count] + "&town_type" + count + "=" + typeName;
                    paths[count] = paths[count] + "&area" + count + "=";
                    paths[count] = paths[count] + "&power" + count + "=" + power;
                    paths[count] = paths[count] + "&king" + count + "=" + king;
                    paths[count] = paths[count] + "&alliance" + count + "=" + alliance;
                    paths[count] = paths[count] + "&population" + count + "=" + population;
                    paths[count] = paths[count] + "&wood_add" + count + "=" + wood_add;
                    paths[count] = paths[count] + "&stone_add" + count + "=" + stone_add;
                    paths[count] = paths[count] + "&iron_add" + count + "=" + iron_add;
                    paths[count] = paths[count] + "&farm_add" + count + "=" + farm_add;
                    paths[count] = paths[count] + "&wood" + count + "=" + wood;
                    paths[count] = paths[count] + "&stone" + count + "=" + stone;
                    paths[count] = paths[count] + "&iron" + count + "=" + iron;
                    paths[count] = paths[count] + "&farm" + count + "=" + farm;
                    if (count == 9) {
                        String postStr = "http://www.lgking.cn/sanguo/lgking_sg_map.php" + "?" + paths[0] + "&" + paths[1] + "&" + paths[2] + "&" + paths[3] + "&" + paths[4] + "&" + paths[5] + "&" + paths[6] + "&" + paths[7] + "&" + paths[8] + "&" + paths[9];
                        System.out.println("url: " + postStr);
                        HttpURLConnection postConnect = (HttpURLConnection) new URL(postStr).openConnection();
                        postConnect.setReadTimeout(160000);
                        postConnect.connect();
                        InputStream inputStream1 = (InputStream) postConnect.getContent();
                        count = 0;
                    } else {
                        count = count + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    x = x - 1;
                }
            }
        }
    }
