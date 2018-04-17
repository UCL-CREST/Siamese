    public int maximumCommonDenominator(int number) {
        int copyA = absoluteValue();
        int copyB = (new IntegerNumber(number)).absoluteValue();
        int temp = 0;
        while (copyB != 0) {
            temp = copyA % copyB;
            copyA = copyB;
            copyB = temp;
        }
        return copyA;
    }
