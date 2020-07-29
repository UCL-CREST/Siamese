    public int countOccurence(String passage, String regex, char rcoat, int largest, float ext) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(passage);
        int openIdx = 0;
        int closeIdx;
        int fullTextLength = passage.length();
        int count = 0;
        while (m.find(openIdx) && m.start() < fullTextLength) {
            closeIdx = passage.indexOf(rcoat, m.start() + 1);
            if (closeIdx == -1 || m.start() == -1 || m.start() + 1 > closeIdx) break;
            String fieldCand = passage.substring(m.start() + 1, closeIdx);
            ArrayList<Integer> idxList = NumberFullTextSpot.checkField(fieldCand, m.start(), m.end(), fullText);
            if (idxList.size() != 0) {
                Collections.sort(idxList);
                if (idxList.get(idxList.size() - 1) <= largest * ext) count++;
            }
            openIdx = closeIdx;
        }
        return count;
    }
