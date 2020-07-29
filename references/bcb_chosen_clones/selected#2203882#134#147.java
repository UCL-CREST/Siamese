    private void runTestMethod(RunNotifier notifier) {
        Description description = this.createDescription();
        Object test;
        try {
            test = this.javaMethod.getDeclaringClass().getConstructor().newInstance();
            this.invokeMethod(test, description, notifier, this.getArguments());
        } catch (InvocationTargetException e) {
            notifier.testAborted(description, e.getCause());
            return;
        } catch (Exception e) {
            notifier.testAborted(description, e);
            return;
        }
    }
