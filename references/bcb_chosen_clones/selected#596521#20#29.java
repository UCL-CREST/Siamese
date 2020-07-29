    public static Collection<String> getFunctionCalls(String eval) throws NullPointerException {
        HashSet<String> uniqueResult = new HashSet<String>();
        Pattern r = Pattern.compile("(?:\\w+\\.)?\\w+(?=\\()");
        Matcher m = r.matcher(eval);
        while (m.find()) uniqueResult.add(eval.substring(m.start(), m.end()));
        ArrayList<String> result = new ArrayList<String>();
        for (String element : uniqueResult) result.add(element);
        Collections.sort(result);
        return result;
    }
