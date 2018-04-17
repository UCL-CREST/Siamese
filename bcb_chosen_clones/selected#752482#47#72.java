    public CompiledScript compile(Script script) throws CompilationException {
        String className = "Exp" + expId.incrementAndGet();
        ExpressionAppender appender;
        try {
            appender = script.expression.apply(createCodeBuilder(script));
        } catch (Throwable e) {
            throw new CompilationException("Error building expression", e);
        }
        byte[] bytecode;
        try {
            bytecode = buildClass(className, appender);
        } catch (Throwable e) {
            throw new CompilationException("Error generating bytecode", e);
        }
        Class<? extends CompiledScript> clazz;
        try {
            clazz = loader.defineClass(className, bytecode);
        } catch (Throwable e) {
            throw new CompilationException("Error defining expression class", e);
        }
        try {
            return clazz.getConstructor(JacexEngine.class).newInstance(engine);
        } catch (Throwable e) {
            throw new CompilationException("Error instantianting expression class", e);
        }
    }
