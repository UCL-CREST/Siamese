    public ExpenditureAccount getElement(int _idExpenditureAccount) {
        int initIndex = 0;
        int endIndex = size() - 1;
        int midIndex = 0;
        while (initIndex <= endIndex) {
            midIndex = (initIndex + endIndex) / 2;
            int idExpType = ((ExpenditureAccount) elementAt(midIndex)).getIDExpenditureAccount();
            if (_idExpenditureAccount == idExpType) {
                break;
            } else if (_idExpenditureAccount > idExpType) {
                initIndex = midIndex + 1;
            } else if (_idExpenditureAccount < idExpType) {
                endIndex = midIndex - 1;
            }
        }
        if (initIndex > endIndex) {
            return null;
        } else {
            indexSelected = midIndex;
            return (ExpenditureAccount) elementAt(midIndex);
        }
    }
