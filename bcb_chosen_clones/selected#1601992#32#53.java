    public Account getElement(int _idAccount) {
        int initIndex = 0;
        int endIndex = size() - 1;
        int midIndex = 0;
        while (initIndex <= endIndex) {
            midIndex = (initIndex + endIndex) / 2;
            int idAccount = ((Account) elementAt(midIndex)).getIDAccount();
            if (_idAccount == idAccount) {
                break;
            } else if (_idAccount > idAccount) {
                initIndex = midIndex + 1;
            } else if (_idAccount < idAccount) {
                endIndex = midIndex - 1;
            }
        }
        if (initIndex > endIndex) {
            return null;
        } else {
            indexSelected = midIndex;
            return (Account) elementAt(midIndex);
        }
    }
