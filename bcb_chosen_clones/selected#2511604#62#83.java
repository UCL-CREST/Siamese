    protected void initFieldsOffset() throws Exception {
        String sepLine = getSperatorLine();
        if (sepLine == null) {
            throw new Exception("The seperation line couldn't be found");
        }
        Pattern p = Pattern.compile("(-+)");
        Matcher m = p.matcher(sepLine);
        int startIndex = 0;
        int[] fields = new int[100];
        int i = 0;
        while (m.find(startIndex)) {
            fields[i] = m.start();
            startIndex = m.end();
            i++;
        }
        numberOfFields = i;
        if (numberOfFields <= 0) {
            throw new Exception("no fields were found in seperation line");
        }
        fieldsOffset = new int[numberOfFields];
        System.arraycopy(fields, 0, fieldsOffset, 0, numberOfFields);
    }
