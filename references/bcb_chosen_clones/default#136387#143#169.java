    private void readVocabularyXMLDocument(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            EW_Languages lang = null;
            EW_Topic commonTopic;
            if (doc.getDocumentElement().getNodeName().equals("language")) {
                lang = createNewLanguage(doc.getDocumentElement().getAttribute("name"));
            }
            NodeList topicList = doc.getElementsByTagName("topic");
            for (int i = 0; i < topicList.getLength(); i++) {
                Element thisTopic = (Element) topicList.item(i);
                commonTopic = createMainTopic(thisTopic.getAttribute("name"), lang);
                NodeList wordList = thisTopic.getElementsByTagName("word");
                for (int j = 0; j < wordList.getLength(); j++) {
                    Element word = (Element) wordList.item(j);
                    NodeList wordTopicsList = word.getElementsByTagName("wordtopic");
                    createWord(lang, commonTopic, wordTopicsList, word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
