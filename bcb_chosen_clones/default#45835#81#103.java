    public static DOMAdapter getDOMAdapter(TabbedView parent, Reader reader) {
        if (reader == null) {
            return null;
        }
        DOMAdapter instance = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(reader));
            doc.getDocumentElement().normalize();
            instance = new DOMAdapter(parent, null, doc, parent.getUntitledLabel());
            adapterList.add(instance);
        } catch (SAXParseException spe) {
            JOptionPane.showMessageDialog(parent, spe, "Parse Error", JOptionPane.WARNING_MESSAGE);
        } catch (SAXException sxe) {
            JOptionPane.showMessageDialog(parent, sxe, "Parse Error", JOptionPane.WARNING_MESSAGE);
        } catch (ParserConfigurationException pce) {
            JOptionPane.showMessageDialog(parent, pce, "Parser Configuration Error", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(parent, ioe, "IO Error", JOptionPane.WARNING_MESSAGE);
        }
        return instance;
    }
