    public ExpenditureAccount.CCItem getElement(int _idExpenditureAccount) {
        int initIndex = 0;
        int endIndex = size() - 1;
        int midIndex = 0;
        while (initIndex <= endIndex) {
            midIndex = (initIndex + endIndex) / 2;
            int idExpAcc = ((ExpenditureAccount.CCItem) elementAt(midIndex)).getIDExpenditureAccount();
            if (_idExpenditureAccount == idExpAcc) {
                break;
            } else if (_idExpenditureAccount > idExpAcc) {
                initIndex = midIndex + 1;
            } else if (_idExpenditureAccount < idExpAcc) {
                endIndex = midIndex - 1;
            }
        }
        if (initIndex > endIndex) {
            return null;
        } else {
            indexSelected = midIndex;
            return (ExpenditureAccount.CCItem) elementAt(midIndex);
        }
    }
