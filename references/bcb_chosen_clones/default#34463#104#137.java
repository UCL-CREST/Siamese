            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("/");
                fileChooser.addChoosableFileFilter(new XmlFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnVal = fileChooser.showOpenDialog(PanFmpGui.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File pathToConfigFile = fileChooser.getSelectedFile();
                    try {
                        searchService = new SearchService(pathToConfigFile.toString());
                        Config conf = new Config(pathToConfigFile.toString(), ConfigMode.SEARCH);
                        Map<String, Config.Config_Field> fields = conf.fields;
                        fieldsCombo.removeAllItems();
                        Iterator<Map.Entry<String, Config.Config_Field>> itFields = fields.entrySet().iterator();
                        while (itFields.hasNext()) {
                            Map.Entry itPair = (Map.Entry) itFields.next();
                            Config_Field fc = (Config_Field) itPair.getValue();
                            if (fc.datatype.equals(DataType.STRING) || fc.datatype.equals(DataType.TOKENIZEDTEXT)) fieldsCombo.addItem(fc.name);
                        }
                        Collection<IndexConfig> indexList = conf.indices.values();
                        virtIndexCombo.removeAllItems();
                        for (IndexConfig iconf : indexList) {
                            if (iconf instanceof VirtualIndexConfig) {
                                VirtualIndexConfig viconf = (VirtualIndexConfig) iconf;
                                virtIndexCombo.addItem(viconf.id);
                            }
                        }
                        PanFmpGui.this.status.setText("Config file successfully loaded");
                    } catch (Exception ex) {
                    }
                    fieldsCombo.setEnabled(true);
                    virtIndexCombo.setEnabled(true);
                    listContentButton.setEnabled(true);
                }
            }
