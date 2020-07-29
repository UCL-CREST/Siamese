    public Collection run() {
        for (Object currentClassObj : _testClasses) {
            boolean hasSetup = false;
            int setupIndex = 0;
            Class currentClass = (Class) currentClassObj;
            Method[] methods = currentClass.getMethods();
            Arrays.sort((Object[]) methods, new methodComparator());
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("setup")) {
                    hasSetup = true;
                    setupIndex = i;
                }
            }
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].isAnnotationPresent(Test.class)) {
                    _className = currentClass.getName();
                    if (_className.contains(".")) _className = _className.substring(_className.lastIndexOf(".") + 1, _className.length());
                    _methodName = methods[i].getName();
                    _currentResult = TestStatus.UNTESTED;
                    try {
                        Constructor constructor = currentClass.getConstructor(new Class[] { this.getClass() });
                        Object newTestObject = constructor.newInstance(new Object[] { this });
                        if (hasSetup) methods[setupIndex].invoke(newTestObject, (Object[]) null);
                        methods[i].invoke(newTestObject, (Object[]) null);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        _currentResult = TestStatus.EXCEPTION;
                        StackTraceElement[] stackTrace = e.getCause().getStackTrace();
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < stackTrace.length; j++) {
                            sb.append(stackTrace[stackTrace.length - 1 - j].toString());
                            sb.append("\n");
                        }
                        sb.append(e.getCause().toString());
                        _exceptionMap.put(methods[i].getName(), sb.toString());
                    }
                    _results.add(new TestResult(_className, _methodName, _currentResult));
                }
            }
        }
        return _results;
    }
