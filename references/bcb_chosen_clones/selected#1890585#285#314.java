    protected void callRunMethod(DaafConfig daafConfig, MethodCall methodCall) throws DaafException, DaafConfigurationException, DaafInterruptedException {
        if (methodCall == null) {
            return;
        }
        try {
            String methodName = StringUtils.trim(methodCall.getName());
            log.info("calling methodName: " + methodName + "...");
            DaafMethodArguments arguments = new DaafMethodArguments(methodCall.getArguments());
            String docbase = daafArgs.getDocbase();
            MethodDefinition methodDefinition = daafConfig.getMethodDefinition(docbase, methodName);
            if (methodDefinition == null) {
                throw new RuntimeException("No method defined for " + methodName);
            }
            String className = StringUtils.trim(methodDefinition.getClassName());
            log.info("...on className: " + className);
            Class methodClass = Class.forName(className);
            Class[] paramTypes = { DaafHelper.class, DaafMethodArguments.class };
            Constructor constructor = methodClass.getConstructor(paramTypes);
            Object[] args = { helper, arguments };
            IDaafMethod wfMethod = (IDaafMethod) constructor.newInstance(args);
            wfMethod.runMethod();
        } catch (DaafInterruptedException die) {
            throw die;
        } catch (DaafException de) {
            throw de;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DaafConfigurationException(e.getMessage(), e);
        }
    }
