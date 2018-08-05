    public ArrayList<String> showTopLetters() {
        int[] tempArray = new int[engCountLetters.length];
        char[] tempArrayLetters = new char[abcEng.length];
        ArrayList<String> resultTopFiveLetters = new ArrayList<String>();
        tempArray = engCountLetters.clone();
        tempArrayLetters = abcEng.clone();
        int tempCount;
        char tempLetters;
        for (int j = 0; j < (abcEng.length * abcEng.length); j++) {
            for (int i = 0; i < abcEng.length - 1; i++) {
                if (tempArray[i] > tempArray[i + 1]) {
                    tempCount = tempArray[i];
                    tempLetters = tempArrayLetters[i];
                    tempArray[i] = tempArray[i + 1];
                    tempArrayLetters[i] = tempArrayLetters[i + 1];
                    tempArray[i + 1] = tempCount;
                    tempArrayLetters[i + 1] = tempLetters;
                }
            }
        }
        for (int i = tempArrayLetters.length - 1; i > tempArrayLetters.length - 6; i--) {
            resultTopFiveLetters.add(tempArrayLetters[i] + ":" + tempArray[i]);
        }
        return resultTopFiveLetters;
    }
