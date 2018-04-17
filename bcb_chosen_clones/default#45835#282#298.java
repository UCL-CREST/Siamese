    public void setSource(TabbedView view, String source) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(source)));
            document.getDocumentElement().normalize();
        } catch (SAXParseException spe) {
            JOptionPane.showMessageDialog(view, spe, "Parse Error", JOptionPane.WARNING_MESSAGE);
        } catch (SAXException sxe) {
            JOptionPane.showMessageDialog(view, sxe, "Parse Error", JOptionPane.WARNING_MESSAGE);
        } catch (ParserConfigurationException pce) {
            JOptionPane.showMessageDialog(view, pce, "Parser Configuration Error", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(view, ioe, "IO Error", JOptionPane.WARNING_MESSAGE);
        }
        fireTreeStructureChanged(new TreeModelEvent(this, new TreePath(document.getDocumentElement())));
    }
