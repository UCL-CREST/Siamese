    @Override
    public void initialize(int idx) {
        try {
            Class<?> testerClass = Class.forName("nz.ac.waikato.modeljunit.QuickTester");
            Constructor<?> con = testerClass.getConstructor(new Class[] { Class.forName("nz.ac.waikato.modeljunit.FsmModel") });
            m_tester[idx] = (QuickTester) con.newInstance(new Object[] { TestExeModel.getModelObject() });
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
