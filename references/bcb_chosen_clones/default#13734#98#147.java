    public void createUnsortedTop50Vector() {
        Hashtable reverseTable = new Hashtable();
        Vector v = new Vector(freqWords.keySet());
        for (Enumeration e = v.elements(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            Double matchingValue = (Double) freqWords.get(key);
            double dMatchingValue = matchingValue.doubleValue();
            if (reverseTable.containsKey(matchingValue)) {
                dMatchingValue = dMatchingValue + 0.1;
                reverseTable.put(new Double(dMatchingValue), key);
            } else {
                reverseTable.put(matchingValue, key);
            }
        }
        List myList = new ArrayList(reverseTable.keySet());
        Collections.sort(myList);
        Collections.reverse(myList);
        int iUpperLimit = 0;
        if (reverseTable.size() < 50) {
            iUpperLimit = reverseTable.size();
        } else {
            iUpperLimit = 50;
        }
        List top50 = myList.subList(0, iUpperLimit);
        Object[] myArray = top50.toArray();
        Vector top50Vector = new Vector();
        for (int iCounter = 0; iCounter < iUpperLimit; iCounter++) {
            top50Vector.addElement(reverseTable.get(myArray[iCounter]));
        }
        String stLine = null;
        String stWord = null;
        double dOccurCount = 0;
        try {
            BufferedReader fl = new BufferedReader(new FileReader(stFilename));
            while ((stLine = fl.readLine()) != null) {
                StringTokenizer line = new StringTokenizer(stLine);
                while (line.hasMoreTokens()) {
                    dNumProcessed++;
                    stWord = line.nextToken();
                    stWord = stWord.toLowerCase();
                    if (top50Vector.contains(stWord)) {
                        unsortedTop50.addElement(stWord);
                    }
                }
            }
            fl.close();
        } catch (IOException e) {
            System.out.println("ERROR READING FILE: " + stFilename);
        }
    }
