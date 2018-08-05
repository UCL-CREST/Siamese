    public ExpressionImpl getExpressionImpl() {
        try {
            String className = Character.toUpperCase(getNodeName().charAt(0)) + getNodeName().substring(1);
            String implClassName;
            if (QTIDOMHelper.isMathQTIElement(getNodeName())) {
                implClassName = "org.servingMathematics.mathqti.domImpl." + className + "Impl";
            } else {
                implClassName = "org.servingMathematics.qti.domImpl." + className + "Impl";
            }
            Constructor constructor = Class.forName(implClassName).getConstructors()[0];
            if (constructor != null) {
                return (ExpressionImpl) constructor.newInstance(new Object[] { this });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
