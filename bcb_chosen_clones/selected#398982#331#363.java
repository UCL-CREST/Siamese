    private static void parseFirstLevel(String part) {
        Pattern p = Pattern.compile("^=([^\r\n]*)=$", Pattern.MULTILINE);
        Matcher m = p.matcher(part);
        int lastHandled = -1;
        int oldend = 0;
        while (m.find()) {
            System.out.println("\nThis section (" + sectionName + ") ends with \n---------------------------\n" + part.substring(oldend, m.start(1) - 1) + "---------------------------");
            int start = m.start(1) - 1;
            addNewSection(part.substring(oldend, start), m.group(1));
            oldend = m.end(1) + 3;
            System.out.print("Going from section " + sectionName + " to " + m.group(1));
            sectionName = m.group(1);
            lastHandled = m.end(1) + 3;
        }
        if (lastHandled == -1) {
            System.out.print(part);
            if (currentPart instanceof Text) {
                ((Text) currentPart).appendText(part);
            } else {
                Text t = new Text(currentPart, part);
                add(t);
            }
        } else if (lastHandled < part.length()) {
            String curr = part.substring(lastHandled, part.length());
            System.out.print("(" + sectionName + ") part: \n---------------------------\n" + curr);
            if (currentPart instanceof Text) {
                ((Text) currentPart).appendText(curr);
            } else if (currentPart instanceof Section) {
                Text t = new Text(currentPart, curr);
                add(t);
            }
        }
    }
