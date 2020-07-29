    public String[] highlightTexts(String contentOftheFile, String query) {
        System.err.println("dasdsdads is" + query);
        String contenttemp = contentOftheFile;
        String contentModified = contenttemp.replaceAll("\n", " ");
        String content = contentModified.replaceAll("\t", " ");
        ArrayList<String> preprocessedModifiedArrayList = new ArrayList<String>();
        String searchQuery = query;
        int startIndex = 0;
        int startIndexTemp = 0;
        int secondWordIndex = 0;
        int endIndex = 0;
        String match = "";
        ArrayList<Integer> indexArrayListforFirstWord = new ArrayList<Integer>();
        ArrayList<Integer> indexArrayListForSecondWord = new ArrayList<Integer>();
        ArrayList<Integer> selectedIndicesFirstWord = new ArrayList<Integer>();
        ArrayList<Integer> selectedIndicesSecondWord = new ArrayList<Integer>();
        String[] indexedInfo = new String[4];
        Pattern pattern = null;
        String[] preprocessedArray = searchQuery.split(" ");
        for (int i = 0; i < preprocessedArray.length; i++) {
            if ((content.indexOf(preprocessedArray[i])) != -1) {
                preprocessedModifiedArrayList.add(preprocessedArray[i]);
            }
        }
        String firstWord = preprocessedModifiedArrayList.get(0);
        String secondWord = preprocessedModifiedArrayList.get(1);
        String modifiedQueryPattern = "";
        modifiedQueryPattern = getSearchQueryPattern(preprocessedModifiedArrayList);
        pattern = Pattern.compile(modifiedQueryPattern);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            startIndex = matcher.start();
            endIndex = matcher.end();
            match = matcher.group();
        }
        startIndexTemp = content.indexOf(firstWord);
        indexArrayListforFirstWord.add(startIndexTemp);
        while (startIndexTemp != -1) {
            startIndexTemp = content.indexOf(firstWord, startIndexTemp + firstWord.length());
            if (startIndexTemp != -1) {
                indexArrayListforFirstWord.add(startIndexTemp);
            }
        }
        secondWordIndex = content.indexOf(secondWord);
        indexArrayListForSecondWord.add(secondWordIndex);
        while (secondWordIndex != -1) {
            secondWordIndex = content.indexOf(secondWord, secondWordIndex + secondWord.length());
            if (secondWordIndex != -1) {
                indexArrayListForSecondWord.add(secondWordIndex);
            }
        }
        for (int j = 0; j < indexArrayListforFirstWord.size(); j++) {
            int distance = endIndex - indexArrayListforFirstWord.get(j);
            if (distance > 0) {
                selectedIndicesFirstWord.add(indexArrayListforFirstWord.get(j));
            }
        }
        for (int j = 0; j < indexArrayListForSecondWord.size(); j++) {
            int distance = endIndex - indexArrayListForSecondWord.get(j);
            if (distance > 0) {
                selectedIndicesSecondWord.add(indexArrayListForSecondWord.get(j));
            }
        }
        try {
            startIndexTemp = this.getMaximum(selectedIndicesFirstWord);
            secondWordIndex = this.getMaximum(selectedIndicesSecondWord);
            if (secondWordIndex > startIndexTemp) {
                startIndex = startIndexTemp;
            } else {
                startIndexTemp = this.getSecondMaximum(selectedIndicesFirstWord);
                startIndex = startIndexTemp;
            }
        } catch (Exception ex) {
        }
        match = content.substring(startIndex, endIndex);
        indexedInfo[0] = String.valueOf(startIndex);
        indexedInfo[1] = String.valueOf(endIndex);
        indexedInfo[2] = match;
        indexedInfo[3] = query;
        return indexedInfo;
    }
