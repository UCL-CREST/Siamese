    private void addLink(ONDEXConcept vertex) {
        Set<ConceptAccession> accs = vertex.getConceptAccessions();
        if (accs.size() > 0) {
            for (ConceptAccession acc : accs) {
                if (!acc.isAmbiguous() && acc.getElementOf().equals(vertex.getElementOf())) {
                    try {
                        new AccessionPlugin(aggregator.getONDEXJUNGGraph());
                    } catch (InvalidPluginArgumentException e) {
                        JOptionPane.showMessageDialog((Component) aggregator, e.getMessage());
                    }
                    String url = AccessionPlugin.cvToURL.get(acc.getElementOf().getId());
                    if (AccessionPlugin.mapper != null) {
                        Condition cond = new Condition(acc.getElementOf().getId(), vertex.getElementOf().getId());
                        String prefix = (String) AccessionPlugin.mapper.validate(cond);
                        if (prefix != null && prefix.length() > 0) {
                            url = prefix;
                        }
                    }
                    if (url != null) {
                        try {
                            final URI uri = new URI(url + "" + acc.getAccession());
                            JMenuItem item = new JMenuItem(acc.getElementOf().getId() + ": " + acc.getAccession());
                            item.setForeground(Color.BLUE);
                            item.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Desktop desktop = null;
                                    if (Desktop.isDesktopSupported()) {
                                        desktop = Desktop.getDesktop();
                                        try {
                                            desktop.browse(uri);
                                        } catch (IOException ioe) {
                                            JOptionPane.showMessageDialog((Component) aggregator, ioe.getMessage());
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog((Component) aggregator, "Hyperlinks not supported by OS.");
                                    }
                                }
                            });
                            this.add(item);
                        } catch (URISyntaxException e1) {
                            JOptionPane.showMessageDialog((Component) aggregator, e1.getMessage());
                        }
                    }
                    break;
                }
            }
        }
    }
