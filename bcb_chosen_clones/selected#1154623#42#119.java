    public final boolean parseAuxFile(String filename) {
        Pattern pattern;
        Matcher matcher;
        boolean weiter = false;
        boolean back = true;
        boolean loopFileOpen = false;
        pattern = Pattern.compile("\\\\citation\\{.+\\}");
        BufferedReader br = null;
        Vector<String> fileList = new Vector<String>(5);
        fileList.add(filename);
        File dummy = new File(filename);
        String path = dummy.getParent();
        if (path != null) path = path + File.separator; else path = "";
        nestedAuxCounter = -1;
        int fileIndex = 0;
        while (fileIndex < fileList.size()) {
            String fName = fileList.elementAt(fileIndex);
            try {
                br = new BufferedReader(new FileReader(fName));
                weiter = true;
                loopFileOpen = true;
            } catch (FileNotFoundException fnfe) {
                System.out.println("Cannot locate input file! " + fnfe.getMessage());
                back = false;
                weiter = false;
                loopFileOpen = false;
            }
            while (weiter) {
                String line;
                try {
                    if (br == null) throw new IOException();
                    line = br.readLine();
                } catch (IOException ioe) {
                    line = null;
                    weiter = false;
                }
                if (line != null) {
                    matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        int len = matcher.end() - matcher.start();
                        if (len > 11) {
                            String str = matcher.group().substring(matcher.start() + 10, matcher.end() - 1);
                            String keys[] = str.split(",");
                            if (keys != null) {
                                int keyCount = keys.length;
                                for (int t = 0; t < keyCount; t++) {
                                    String dummyStr = keys[t];
                                    if (dummyStr != null) {
                                        mySet.add(dummyStr.trim());
                                    }
                                }
                            }
                        }
                    }
                    int index = line.indexOf("\\@input{");
                    if (index >= 0) {
                        int start = index + 8;
                        int end = line.indexOf("}", start);
                        if (end > start) {
                            String str = path + line.substring(index + 8, end);
                            if (!fileList.contains(str)) {
                                fileList.add(str);
                            }
                        }
                    }
                } else weiter = false;
            }
            if (loopFileOpen) {
                try {
                    if (br != null) br.close();
                    nestedAuxCounter++;
                } catch (IOException ioe) {
                }
            }
            fileIndex++;
        }
        return back;
    }
