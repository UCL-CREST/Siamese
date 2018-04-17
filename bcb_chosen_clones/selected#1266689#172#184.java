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
