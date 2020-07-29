    void makeModel(Event template) {
        data = new ArrayList[] { new ArrayList(), new ArrayList() };
        int extraRowCount = 10;
        if (template != null) {
            String[] names = template.getPropertyNames();
            for (int i = 0; i < names.length; i++) {
                data[0].add(names[i]);
                data[1].add(template.getProperty(names[i]));
            }
            extraRowCount = 3;
            topicC.setSelectedItem(template.getTopic());
        }
        for (int i = 0; i < extraRowCount; i++) {
            data[0].add("");
            data[1].add("");
        }
        AbstractTableModel model = new AbstractTableModel() {

            public String getColumnName(int column) {
                return column == 0 ? "Name" : "Value";
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }

            public int getColumnCount() {
                return 2;
            }

            public int getRowCount() {
                return data[0].size();
            }

            public Object getValueAt(int row, int col) {
                return data[col].get(row);
            }

            public void setValueAt(Object val, int row, int col) {
                Object oldVal = data[col].get(row);
                if (oldVal != null && val != null) {
                    if (oldVal.getClass() != val.getClass()) {
                        try {
                            Object val2 = oldVal.getClass().getConstructor(new Class[] { String.class }).newInstance(new Object[] { val.toString() });
                            val = val2;
                        } catch (Exception e) {
                        }
                    }
                }
                data[col].set(row, val);
            }
        };
        propTable.setModel(model);
    }
