    private static String calculateScenarioMD5(Scenario scenario) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Vector<JTest> allTests = scenario.getTests();
        for (JTest t : allTests) {
            String name = t.getTestName() + t.getTestId();
            String parameters = "";
            if (t instanceof RunnerTest) {
                parameters = ((RunnerTest) t).getPropertiesAsString();
            }
            md.update(name.getBytes());
            md.update(parameters.getBytes());
        }
        byte[] hash = md.digest();
        BigInteger result = new BigInteger(hash);
        String rc = result.toString(16);
        return rc;
    }
