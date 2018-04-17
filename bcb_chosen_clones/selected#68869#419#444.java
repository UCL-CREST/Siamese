        public SQLStatement find(String word, SimpleSQLTokenizer tokenizer) {
            if (!isSorted) {
                Collections.sort(statements, new Comparator<SQLStatement>() {

                    public int compare(SQLStatement s1, SQLStatement s2) {
                        return s1.toString().compareTo(s2.toString());
                    }
                });
                isSorted = true;
            }
            int low = 0;
            int high = statements.size() - 1;
            while (low < high) {
                int mid = (low + high) / 2;
                SQLStatement s = statements.get(mid);
                int rc = s.compare(word, tokenizer);
                if (rc > 0) {
                    high = mid - 1;
                } else if (rc < 0) {
                    low = mid + 1;
                } else {
                    return s;
                }
            }
            return null;
        }
