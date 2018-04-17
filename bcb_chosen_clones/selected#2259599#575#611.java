    private void parseParents() {
        myTokens = new ArrayList();
        String regExp = "(/((/)|(\\d/))?)|\\*|\\(|\\)|,";
        System.out.println("\nregular expression: " + regExp + "\n");
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regExp);
        Matcher matcher = pattern.matcher("null");
        Iterator itr = myRecords.iterator();
        while (itr.hasNext()) {
            String[] current = (String[]) itr.next();
            String parents = current[PARENTS_INDEX];
            List tokens = new ArrayList();
            matcher = matcher.reset(parents);
            int position = 0;
            while (matcher.find()) {
                if (matcher.start() != position) {
                    String temp = parents.substring(position, matcher.start());
                    temp = temp.trim();
                    if (temp.length() != 0) {
                        tokens.add(temp);
                    }
                }
                tokens.add(Token.getInstance(matcher.group()));
                position = matcher.end();
            }
            if (position != parents.length()) {
                String temp = parents.substring(position, parents.length());
                temp = temp.trim();
                if (temp.length() != 0) {
                    tokens.add(temp);
                }
            }
            if ((tokens.size() == 0) && (parents.length() != 0)) {
                tokens.add(parents);
            }
            myTokens.add(tokens);
        }
    }
