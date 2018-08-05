    public static DOMAdapter getDOMAdapter(TabbedView parent, File file) {
        if (file == null) {
            return null;
        }
        DOMAdapter instance = null;
        if (file.exists()) {
            for (int i = 0; i < adapterList.size(); i++) {
                DOMAdapter currentAdapter = (DOMAdapter) adapterList.get(i);
                File currentFile = currentAdapter.getFile();
                if (currentFile != null) {
                    try {
                        if (file.getCanonicalPath().equals(currentAdapter.getFile().getCanonicalPath())) {
                            return currentAdapter;
                        }
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(parent, ioe, "IO Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);
                doc.getDocumentElement().normalize();
                instance = new DOMAdapter(parent, file, doc, file.getName());
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
        } else {
            JOptionPane.showMessageDialog(parent, "Could not open file " + file.getName(), "IO Error", JOptionPane.WARNING_MESSAGE);
        }
        return instance;
    }
