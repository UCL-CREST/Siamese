    static Vector<GroupItem> loadConfig() {
        Vector<GroupItem> groups = new Vector<GroupItem>();
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder;
        try {
            File cFile = new File(configFileLocation);
            if (!cFile.exists()) {
                cFile.createNewFile();
                writeConfig(groups);
                return groups;
            }
            domBuilder = domFactory.newDocumentBuilder();
            Document configFile = domBuilder.parse(new File(configFileLocation));
            NodeList nodes = configFile.getElementsByTagName("group");
            ;
            for (int index = 0; index < nodes.getLength(); ++index) {
                Node currentGroup = nodes.item(index);
                String groupName = getGroupName((Element) currentGroup);
                String fileName = getFileName((Element) currentGroup);
                groups.add(new GroupItem(groupName, fileName));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groups;
    }
