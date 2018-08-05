    protected TestSuite getTestSuite() throws Exception {
        TestSuite suite = new TestSuite();
        Context context = Context.ContextFactory.getContext();
        List<Site> sites = null;
        try {
            sites = context.getGrid().getAllSites();
        } catch (RoctopusException e) {
            e.printStackTrace();
        }
        for (Site site : sites) {
            Constructor con = this.getClass().getConstructor(new Class[] { String.class });
            Test test = (Test) con.newInstance("testOneSite");
            ((OneSiteInjectable) test).setSite1(site);
            suite.addTest(test);
        }
        return suite;
    }
