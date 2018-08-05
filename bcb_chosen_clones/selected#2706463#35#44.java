    @Override
    public void initialize(int idx) {
        try {
            Class<?> testerClass = Class.forName("nz.ac.waikato.modeljunit.RandomTester");
            Constructor<?> con = testerClass.getConstructor(new Class[] { Class.forName("nz.ac.waikato.modeljunit.FsmModel") });
            m_tester[idx] = (RandomTester) con.newInstance(new Object[] { TestExeModel.getModelObject() });
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
