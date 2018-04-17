    protected void processGroups(String conditionValue) {
        ArrayList<groupBoundary> foundGroups = new ArrayList<groupBoundary>();
        if (conditionValue.contains(GROUP_BEGIN) && conditionValue.contains(GROUP_END)) {
            Pattern pGroup = Pattern.compile("\\{(.+?)\\}");
            Matcher m = pGroup.matcher(conditionValue);
            while (m.find()) {
                int ns = m.start();
                int ne = m.end();
                foundGroups.add(new groupBoundary(ns, ne));
            }
            processFoundGroups(foundGroups, conditionValue);
        } else {
            processOperators(conditionValue);
        }
    }
