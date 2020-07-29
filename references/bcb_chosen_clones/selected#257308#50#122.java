    public Stream getStreamValue(StreamContext context) {
        Stream candidate = context.getStreamIfPresent(this);
        if (candidate != null) {
            return candidate;
        }
        String name;
        if (nameExpr == null) {
            name = "_" + System.nanoTime();
        } else {
            name = nameExpr.getStringValue(context);
        }
        String className = classExpr.getStringValue(context);
        Class<?> streamClass = null;
        if (className == null) {
            streamClass = EventStream.class;
        } else {
            if (className.indexOf('.') == -1) {
                int prefixIndex = 0;
                while (streamClass == null && prefixIndex < STREAM_CLASS_PREFIXES.length) {
                    String fullClassName = STREAM_CLASS_PREFIXES[prefixIndex++] + className;
                    try {
                        streamClass = Class.forName(fullClassName);
                    } catch (ClassNotFoundException e) {
                    }
                }
            } else {
                try {
                    streamClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                }
            }
            if (streamClass == null) {
                throw new IllegalArgumentException("Could not resolve " + className + " needed for stream construction");
            }
        }
        if (!Stream.class.isAssignableFrom(streamClass)) {
            throw new IllegalArgumentException("Class " + streamClass.getName() + " was specified as stream type but is not a subclass of Stream");
        }
        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(String.class, StreamContext.class, TupleExpression.class, Unit.class);
        } catch (SecurityException e) {
            cons = null;
        } catch (NoSuchMethodException e) {
            cons = null;
        }
        if (cons == null) {
            throw new IllegalArgumentException("Class " + streamClass.getName() + " was specified as stream type but does not have a ForkTalk constructor");
        }
        Throwable exception = null;
        try {
            Stream ans = (Stream) cons.newInstance(name, tupleExpr.getStreamContext(), tupleExpr, getUnitFrom(tupleExpr.getType()));
            if (nameExpr == null) {
                ans.setInvisible(true);
            }
            return ans;
        } catch (InvocationTargetException e) {
            exception = e.getCause();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Class " + streamClass.getName() + " was specified as stream type but it's ForkTalk constructor is not public");
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Class " + streamClass.getName() + " was specified as stream type but is abstract");
        } catch (Exception e) {
            exception = e;
        }
        if (exception instanceof RuntimeException) {
            throw (RuntimeException) exception;
        } else if (exception instanceof Error) {
            throw (Error) exception;
        } else {
            throw new IllegalArgumentException(exception.getMessage(), exception);
        }
    }
