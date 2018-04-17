    private void initSource() {
        try {
            for (int i = 0; i < NUM_TRANSFORMATIONS; i++) {
                final String sourceXMLURI = (new File(XML_IN_BASE + i + XML_EXT)).toURL().toString();
                m_inStream[i] = new FileInputStream(XML_IN_BASE + i + XML_EXT);
                switch(SOURCE_FLAVOR) {
                    case STREAM:
                        m_inSource[i] = new StreamSource(m_inStream[i]);
                        break;
                    case SAX:
                        m_inSource[i] = new SAXSource(new InputSource(m_inStream[i]));
                        break;
                    case DOM:
                        try {
                            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                            dfactory.setNamespaceAware(true);
                            m_inSource[i] = new DOMSource(dfactory.newDocumentBuilder().parse(m_inStream[i]));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                if (m_inSource[i] != null) {
                    m_inSource[i].setSystemId(sourceXMLURI);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
