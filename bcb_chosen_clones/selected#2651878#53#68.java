    protected Object getCatalogObject(DVector list, String name, int dim) {
        synchronized (list) {
            try {
                if (list.size() == 0) return null;
                int start = 0;
                int end = list.size() - 1;
                while (start <= end) {
                    int mid = (end + start) / 2;
                    int comp = ((Environmental) list.elementAt(mid, 1)).Name().compareToIgnoreCase(name);
                    if (comp == 0) return list.elementAt(mid, dim); else if (comp > 0) end = mid - 1; else start = mid + 1;
                }
            } catch (Exception e) {
            }
            return null;
        }
    }
