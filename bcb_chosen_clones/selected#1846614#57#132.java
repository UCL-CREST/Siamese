    public OCT2007Reader(File file) throws IOException, NotRightFormatException {
        this.file = file;
        fIS = new FileInputStream(file);
        int length = (int) file.length();
        byte[] s = null;
        try {
            s = new byte[length];
        } catch (java.lang.OutOfMemoryError e) {
            throw new IOException("File too long to fit into memory.");
        }
        fIS.read(s);
        try {
            all = new String(s, encoding);
        } catch (UnsupportedEncodingException e) {
            all = "";
        }
        String firstLineOfPattern = "[ \\t]*\\d+[ \\t]+\\d+[ \\t\\S]*([ \\t]+(\\d{1,2}:)?\\d\\d:\\d\\d)+[ \\t]*";
        String secondLineOfPattern = "([ \\t]+(\\d{1,2}:)?\\d\\d:\\d\\d)+[ \\t]*";
        String thirdLineOfPattern = "---+[ \\t]*";
        Pattern hyphenPattern = Pattern.compile(thirdLineOfPattern + "\\r\\n");
        Matcher hyphenMatcher = hyphenPattern.matcher(all);
        Pattern athletePattern = Pattern.compile(firstLineOfPattern + "\\r\\n" + secondLineOfPattern + "\\r\\n" + thirdLineOfPattern + "\\r\\n");
        Matcher athleteMatcher = athletePattern.matcher(all);
        if (!hyphenMatcher.find()) {
            throw new NotRightFormatException(file, "OCT2007", "it not contains line of hyphens");
        } else {
            if (!athleteMatcher.find()) {
                throw new NotRightFormatException(file, "OCT2007", "it not contains athlete pattern");
            } else {
                if (!athleteMatcher.find(athleteMatcher.end())) {
                    throw new NotRightFormatException(file, "OCT2007", "it not contains two sequential athlete pattern");
                }
            }
        }
        Pattern groupPattern = Pattern.compile("^\\s+([^-\\s:]+)\\s*(\\r\\n)+", Pattern.MULTILINE);
        Matcher groupMatcher = groupPattern.matcher(all);
        groupsNames = new Vector<String>();
        allGroups = new Vector<Group>();
        int groupPatternIndex = 0;
        while (groupMatcher.find(groupPatternIndex)) {
            allGroups.add(new Group());
            String groupName = groupMatcher.group(1);
            groupsNames.add(groupName);
            allGroups.lastElement().setName(groupName);
            int curIndex = groupMatcher.start();
            int endIndex = 0;
            if (groupMatcher.find(groupMatcher.end())) {
                endIndex = groupMatcher.start();
            } else {
                endIndex = all.length() - 1;
            }
            numberOfPoints = 0;
            boolean athleteFinded = true;
            while ((curIndex < endIndex) && (athleteFinded)) {
                athleteFinded = false;
                if (athleteMatcher.find(curIndex)) {
                    curIndex = athleteMatcher.start();
                    if (curIndex < endIndex) {
                        parseAthlete(athleteMatcher.group(), allGroups.lastElement());
                        athleteFinded = true;
                        curIndex = athleteMatcher.end();
                    }
                }
            }
            if (allGroups.lastElement().getAthletes().size() == 0) {
                allGroups.remove(allGroups.lastElement());
                groupsNames.remove(groupsNames.lastElement());
            } else {
                int groupDistance = 5000;
                Distance d = new Distance(groupName, groupDistance, numberOfPoints);
                allGroups.lastElement().setDistance(d);
                d.setLengthsOfDists(Tools.calculatLengthsOfLaps(d.getGroups()));
            }
            groupPatternIndex = endIndex;
        }
    }
