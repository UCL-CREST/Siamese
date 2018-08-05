    private void reRunTestMethod() throws Exception {
        Object test = this.javaMethod.getDeclaringClass().getConstructor().newInstance();
        this.returnValue = this.javaMethod.invoke(test, this.getArguments());
    }
