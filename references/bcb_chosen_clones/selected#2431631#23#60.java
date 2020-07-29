    public void runTest() throws Exception {
        String methodName = super.getTestMethodContext().getName();
        Vector<ParameterContext> params = super.getTestMethodContext().getParameters();
        Class[] signature = new Class[0];
        Object[] values = new Object[0];
        if (params != null) {
            signature = new Class[params.size()];
            values = new Object[params.size()];
            for (ParameterContext param : params) {
                Object value = super.createParameter(param);
                signature[param.getIndex()] = super.getParameterClass(param);
                values[param.getIndex()] = value;
            }
        }
        if (methodName.equals(fShortClassName)) {
            if (fLogger.isDebugEnabled()) fLogger.debug("Calling constructor: " + super.getTestCaseContext().getEndPoint());
            String pojoClassName = super.getTestCaseContext().getEndPoint();
            Class pojoClass = Class.forName(pojoClassName);
            Constructor constructor = pojoClass.getConstructor(signature);
            Object pojo = constructor.newInstance(values);
            super.getTestCaseContext().addProperty("PojoTestDriver.pojo", pojo);
        } else {
            Object pojo = super.getTestCaseContext().getPropertyValue("PojoTestDriver.pojo");
            if (pojo == null) {
                String errMsg = "Missing pojo object: " + super.getTestCaseContext().getEndPoint();
                fLogger.error(errMsg);
                throw new AssertionFailedError(errMsg);
            } else {
                Method method = pojo.getClass().getMethod(methodName, signature);
                try {
                    Object retValue = method.invoke(pojo, values);
                    super.checkResult(retValue);
                } catch (Throwable exc) {
                    super.checkException(exc);
                }
            }
        }
    }
