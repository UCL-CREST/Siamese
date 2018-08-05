    public Keyword get(String keywords) {
        int left = 0;
        int right = list.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (getKeywordString(mid).compareTo(keywords) < 0) left = mid + 1; else if (getKeywordString(mid).compareTo(keywords) > 0) right = mid - 1; else return get(mid);
        }
        return null;
    }
