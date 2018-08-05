    private int binarySearch(ArrayList<Relation> list, Relation rel) {
        int result = -1;
        if (list != null && rel != null) {
            int start = 0;
            int end = list.size() - 1;
            int mid = (end - start) / 2;
            while (start <= end) {
                Relation midRel = list.get(mid);
                if (midRel.getRow() == rel.getRow() && (midRel.getCol() == rel.getCol() || rel.getCol() == -1)) {
                    result = mid;
                    return result;
                } else if (rel.getRow() > midRel.getRow() || rel.getRow() == midRel.getRow() && rel.getCol() > midRel.getCol()) start = mid + 1; else if (rel.getRow() < midRel.getRow() || rel.getRow() == midRel.getRow() && rel.getCol() < midRel.getCol()) end = mid - 1;
                if (start <= end) mid = (start + end) / 2;
            }
            if (mid == 0) {
                result = Integer.MIN_VALUE;
            } else result = -mid;
        }
        return result;
    }
