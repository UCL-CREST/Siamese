    private void binarySearchInsert(int rowid, RowComponent new_comp) {
        if (rowid > last_active) last_active = rowid;
        int num_rows = row_components.size();
        int front = 0;
        int end = num_rows - 1;
        if (num_rows <= 0) {
            printMessage("inserting first row " + rowid);
            ArrayList new_list = new ArrayList();
            new_list.add(new_comp);
            if (new_list.size() > max_row_size) max_row_size = new_list.size();
            row_components.add(new_list);
            row_alignments.put(new Integer(rowid), LEFT);
            return;
        }
        while (front < end) {
            int mid = (front + end) / 2;
            RowComponent r_comp = (RowComponent) ((ArrayList) row_components.get(mid)).get(0);
            if (r_comp.getRowID() == rowid) {
                end = mid;
                front = mid;
            } else if (rowid < r_comp.getRowID()) {
                end = mid - 1;
            } else if (rowid > r_comp.getRowID()) {
                front = mid + 1;
            }
        }
        RowComponent r_comp = (RowComponent) ((ArrayList) row_components.get(front)).get(0);
        if (rowid == r_comp.getRowID()) {
            printMessage("inserting to existing row " + rowid);
            ((ArrayList) row_components.get(front)).add(new_comp);
            if (((ArrayList) row_components.get(front)).size() > max_row_size) max_row_size = ((ArrayList) row_components.get(front)).size();
        } else {
            insertNewRow(rowid, front, new_comp);
        }
    }
