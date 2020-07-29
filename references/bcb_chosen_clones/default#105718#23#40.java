    public ArrayList<iTest> createTests(ArrayList<HashMap> testArray) {
        ArrayList<iTest> tests = new ArrayList();
        Iterator iter = testArray.iterator();
        while (iter.hasNext()) {
            HashMap testParams = (HashMap) iter.next();
            String className = (String) testParams.get("name");
            try {
                Class temp = Class.forName(className);
                Constructor con = temp.getConstructor(HashMap.class);
                Object o = con.newInstance(new Object[] { testParams });
                iTest test = (iTest) o;
                tests.add(test);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        return tests;
    }
