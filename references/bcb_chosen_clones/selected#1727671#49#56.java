    private void runAnnotatedTestClass(Class<?> testClass, RunWith runWith) throws Exception {
        Class<? extends Runner> runnerClass = runWith.value();
        Constructor<? extends Runner> constructor = runnerClass.getConstructor(Class.class);
        Runner runner = constructor.newInstance(testClass);
        RunNotifier notifier = new RunNotifier();
        notifier.addListener(new MyListener());
        runner.run(notifier);
    }
