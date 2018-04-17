    public void setValueAt(Object value, int row, int col) {
        Attribute att = (Attribute) item.getAttribute(row);
        switch(col) {
            case 0:
                int index = item.getAttributeIndex(att.getName());
                item.removeAttribute(index);
                att.setName((String) value);
                item.addAttribute(index, att);
                break;
            case 1:
                att.setType(((Type) value).type);
                break;
            case 2:
                try {
                    if (att.getType() == Character.class) {
                        String str = (String) value;
                        if (str.length() > 0) {
                            att.setValue(new Character(str.charAt(0)));
                        } else {
                            att.setValue(null);
                        }
                    } else {
                        String str = (String) value;
                        if (str.length() > 0) {
                            Constructor c = att.getType().getConstructor(new Class[] { String.class });
                            att.setValue(c.newInstance(new Object[] { value }));
                        } else {
                            att.setValue(null);
                        }
                    }
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
                break;
        }
        fireTableCellUpdated(row, col);
    }
