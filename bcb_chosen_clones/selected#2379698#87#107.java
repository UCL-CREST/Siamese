    private void binarySearchForWordAndPartOfSpeechOccurrenceCounterAndUpdateRecipient(Turn t, LexiconEntry lx1) {
        int lowestPossibleLoc = 0;
        int highestPossibleLoc = wordsReceived.size() - 1;
        int middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
        while (highestPossibleLoc >= lowestPossibleLoc) {
            middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
            WordPartOfSpeechOccurrenceCounterRecipient wposoccr = (WordPartOfSpeechOccurrenceCounterRecipient) wordsReceived.elementAt(middle);
            LexiconEntry lxe = wposoccr.getLexEntry();
            int comparator = (lx1.getWord() + lx1.getPartOfSpeech()).compareToIgnoreCase(lxe.getWord() + lxe.getPartOfSpeech());
            if (comparator == 0) {
                wposoccr.update(t);
                return;
            } else if (comparator < 0) {
                highestPossibleLoc = middle - 1;
            } else {
                lowestPossibleLoc = middle + 1;
            }
        }
        WordPartOfSpeechOccurrenceCounterRecipient wposoccr = new WordPartOfSpeechOccurrenceCounterRecipient(t, lx1, this);
        wordsReceived.insertElementAt(wposoccr, lowestPossibleLoc);
    }
