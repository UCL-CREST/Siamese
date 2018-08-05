                return EMPTY_ITERATOR;
            }
            node = edge.getChild();
            String label = edge.getLabel();
            int j = match(prefix, i, stopOffset, label);
            if (i + j == stopOffset) {
                break;
            } else if (j >= 0) {
                node = null;
                break;
            } else {
