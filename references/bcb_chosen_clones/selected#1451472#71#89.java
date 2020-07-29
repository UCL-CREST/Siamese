    private Task createDecoratedTask(final String decoratedTaskClassName, final String executionMethodName, final Object taskInstance) throws Exception {
        final ClassFab fab = _classFactory.newClass(decoratedTaskClassName, DecoratedTask.class);
        fab.addField("_taskInstance", taskInstance.getClass());
        final BodyBuilder bodyBuilder = new BodyBuilder();
        bodyBuilder.begin();
        bodyBuilder.addln("_taskInstance." + executionMethodName + "();");
        bodyBuilder.end();
        fab.addMethod(java.lang.reflect.Modifier.PUBLIC, new MethodSignature(void.class, "doExecuteTask", new Class[0], new Class[] { Throwable.class }), bodyBuilder.toString());
        bodyBuilder.clear();
        processMethods(taskInstance.getClass(), fab);
        bodyBuilder.begin();
        bodyBuilder.addln("super();");
        bodyBuilder.addln("_taskInstance = $1;");
        bodyBuilder.end();
        fab.addConstructor(new Class[] { taskInstance.getClass() }, new Class[0], bodyBuilder.toString());
        final Class<Task> decoratedTask = fab.createClass();
        final Constructor<Task> constructor = decoratedTask.getConstructor(taskInstance.getClass());
        return constructor.newInstance(taskInstance);
    }
