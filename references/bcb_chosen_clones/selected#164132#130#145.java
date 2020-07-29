    public String calculateProjectMD5(String scenarioName) throws Exception {
        Scenario s = ScenariosManager.getInstance().getScenario(scenarioName);
        s.loadParametersAndValues();
        String scenarioMD5 = calculateScenarioMD5(s);
        Map<ProjectComponent, String> map = getProjectMD5(new ProjectComponent[] { ProjectComponent.resources, ProjectComponent.classes, ProjectComponent.suts, ProjectComponent.libs });
        map.put(ProjectComponent.currentScenario, scenarioMD5);
        MessageDigest md = MessageDigest.getInstance("MD5");
        Iterator<String> iter = map.values().iterator();
        while (iter.hasNext()) {
            md.update(iter.next().getBytes());
        }
        byte[] hash = md.digest();
        BigInteger result = new BigInteger(hash);
        String rc = result.toString(16);
        return rc;
    }
