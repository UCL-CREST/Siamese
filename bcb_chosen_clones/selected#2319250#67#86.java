    private LexiconEntry binarySearchForWordAndPartOfSpeechAndUpdate(TaggedWord tw) {
        int lowestPossibleLoc = 0;
        int highestPossibleLoc = lexicon.size() - 1;
        int middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
        while (highestPossibleLoc >= lowestPossibleLoc) {
            middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
            LexiconEntry lxe = (LexiconEntry) lexicon.elementAt(middle);
            int comparator = (tw.word() + tw.tag()).compareToIgnoreCase(lxe.getWord() + lxe.getPartOfSpeech());
            if (comparator == 0) {
                return lxe;
            } else if (comparator < 0) {
                highestPossibleLoc = middle - 1;
            } else {
                lowestPossibleLoc = middle + 1;
            }
        }
        LexiconEntry le = new LexiconEntry(tw.word(), tw.tag());
        lexicon.insertElementAt(le, lowestPossibleLoc);
        return le;
    }
