    private void binarySearchForWordAndPartOfSpeechOccurrenceCounterAndUpdateSender(Turn t, LexiconEntry lx1) {
        int lowestPossibleLoc = 0;
        int highestPossibleLoc = wordsUsed.size() - 1;
        int middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
        while (highestPossibleLoc >= lowestPossibleLoc) {
            middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
            WordPartOfSpeechOccurrenceCounterSender wposocc = (WordPartOfSpeechOccurrenceCounterSender) wordsUsed.elementAt(middle);
            LexiconEntry lxe = wposocc.getLexEntry();
            int comparator = (lx1.getWord() + lx1.getPartOfSpeech()).compareToIgnoreCase(lxe.getWord() + lxe.getPartOfSpeech());
            if (comparator == 0) {
                wposocc.update(t);
                return;
            } else if (comparator < 0) {
                highestPossibleLoc = middle - 1;
            } else {
                lowestPossibleLoc = middle + 1;
            }
        }
        WordPartOfSpeechOccurrenceCounterSender wposoccc = new WordPartOfSpeechOccurrenceCounterSender(t, lx1);
        wordsUsed.insertElementAt(wposoccc, lowestPossibleLoc);
    }
