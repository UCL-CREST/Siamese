    public static void main(String[] a) throws Exception {
        HashMap<String, Integer> numberOfOccurencesOfThisComboe = new HashMap<String, Integer>();
        HashMap<String, String> fileToCATHMapping = new HashMap<String, String>();
        ArrayList<String> allFilesToBeCopied = new ArrayList<String>();
        new File(outputDir).mkdirs();
        FileReader fis = new FileReader(completeFileWithDirToCathFileList);
        BufferedReader bis = new BufferedReader(fis);
        String line = "";
        String currentCombo = "";
        while ((line = bis.readLine()) != null) {
            String[] allEntries = line.split("\\s+");
            String fileName = allEntries[0];
            String thisCombo = allEntries[1] + allEntries[2] + allEntries[3] + allEntries[4];
            String reducedComboForFilteringOut = allEntries[1] + allEntries[2] + allEntries[3];
            fileToCATHMapping.put(fileName, reducedComboForFilteringOut);
            if (currentCombo.equals(thisCombo)) {
            } else {
                System.out.println("merke: " + fileName);
                allFilesToBeCopied.add(fileName);
                currentCombo = thisCombo;
            }
        }
        for (String fileName : allFilesToBeCopied) {
            String reducedComboForFilteringOut = fileToCATHMapping.get(fileName);
            if (!numberOfOccurencesOfThisComboe.containsKey(reducedComboForFilteringOut)) {
                numberOfOccurencesOfThisComboe.put(reducedComboForFilteringOut, 1);
            } else {
                Integer thisCounter = numberOfOccurencesOfThisComboe.get(reducedComboForFilteringOut);
                thisCounter = thisCounter + 1;
                numberOfOccurencesOfThisComboe.put(reducedComboForFilteringOut, thisCounter);
            }
        }
        HashSet<String> isSingleElement = new HashSet<String>();
        for (Entry<String, Integer> thisEntry : numberOfOccurencesOfThisComboe.entrySet()) {
            if (thisEntry.getValue() == 1) {
                System.out.println("single: " + thisEntry.getKey());
                isSingleElement.add(thisEntry.getKey());
            } else {
                System.out.println("not single: " + thisEntry.getKey());
            }
        }
        System.out.println(allFilesToBeCopied.size());
        for (String file : allFilesToBeCopied) {
            if (!isSingleElement.contains(fileToCATHMapping.get(file))) {
                try {
                    FileChannel srcChannel = new FileInputStream(CathDir + file).getChannel();
                    FileChannel dstChannel = new FileOutputStream(outputDir + file).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
