    byte[] toByteArray() throws IOException {
        DataElement element = new DataElement(DataElement.DATSEQ);
        final boolean sort = true;
        if (sort) {
            int[] sortIDs = new int[attributes.size()];
            int k = 0;
            for (Enumeration e = attributes.keys(); e.hasMoreElements(); ) {
                Integer key = (Integer) e.nextElement();
                sortIDs[k] = key.intValue();
                k++;
            }
            for (int i = 0; i < sortIDs.length; i++) {
                for (int j = 0; j < sortIDs.length - i - 1; j++) {
                    if (sortIDs[j] > sortIDs[j + 1]) {
                        int temp = sortIDs[j];
                        sortIDs[j] = sortIDs[j + 1];
                        sortIDs[j + 1] = temp;
                    }
                }
            }
            for (int i = 0; i < sortIDs.length; i++) {
                element.addElement(new DataElement(DataElement.U_INT_2, sortIDs[i]));
                element.addElement(getAttributeValue(sortIDs[i]));
            }
        } else {
            for (Enumeration e = attributes.keys(); e.hasMoreElements(); ) {
                Integer key = (Integer) e.nextElement();
                element.addElement(new DataElement(DataElement.U_INT_2, key.intValue()));
                element.addElement((DataElement) attributes.get(key));
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        (new SDPOutputStream(out)).writeElement(element);
        return out.toByteArray();
    }
