    @Override
    protected SEXP doEval() {
        try {
            return (SEXP) Class.forName(className).getConstructor(Environment.class).newInstance(environment);
        } catch (Exception e) {
            throw new EvalException("Could not load class '" + className + "'");
        }
    }
