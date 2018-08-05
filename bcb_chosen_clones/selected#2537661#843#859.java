    public int removeFiles(File root, JProgressBar toUpdate, int min, int max, int numOfFiles) {
        int answer = 1;
        File[] fileList = null;
        if (root == null) return 0;
        if (numOfFiles == 0) return 0;
        if (root.isDirectory()) {
            fileList = root.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                answer += removeFiles(fileList[i], toUpdate, min, max, numOfFiles);
            }
        }
        root.delete();
        globalCount++;
        toUpdate.setValue(min + ((globalCount * (max - min) / numOfFiles)));
        mainPanel.validate();
        return answer;
    }
