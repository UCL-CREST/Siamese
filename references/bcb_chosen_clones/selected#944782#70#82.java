    public ExecutableFunctionExpression(ExecutableFunctionExpression copy) throws CloneNotSupportedException {
        try {
            Constructor<? extends ExecutableFunction> constructor = copy.mFunction.getClass().getConstructor(copy.mFunction.getClass());
            mFunction = constructor.newInstance(copy.mFunction);
            List<ArithmeticExpression> exprList = new ArrayList<ArithmeticExpression>();
            for (ArithmeticExpression e : copy.mFunction.getParameters()) {
                exprList.add(CloneArithmeticExprVisitor.cloneExpression(e));
            }
            mFunction.initialise(exprList);
        } catch (Exception e) {
            throw new CloneNotSupportedException();
        }
    }
