    public void evaluate() {
        try {
            if (evalClass != null && this.selectedModel != null) {
                Constructor cons = evalClass.getConstructor(new Class[] { RefPackage.class });
                Object evaluator = cons.newInstance(new Object[] { selectedModel.model });
                results.clear();
                Method getConstraintNames = evaluator.getClass().getDeclaredMethod("getConstraintNames", null);
                Method evaluate = evaluator.getClass().getDeclaredMethod("evaluate", new Class[] { String.class });
                Object oConstraintNames = getConstraintNames.invoke(evaluator, null);
                String[] constraintNames = (String[]) oConstraintNames;
                for (int i = 0; i < constraintNames.length; i++) {
                    Object result = evaluate.invoke(evaluator, new Object[] { constraintNames[i] });
                    results.addRow(constraintNames[i], result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
