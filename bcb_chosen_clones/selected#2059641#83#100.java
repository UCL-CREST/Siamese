    private void setElement(OgnlContext ctx, Object target, int index, Object value) throws OgnlException {
        int len = Array.getLength(target);
        if (index >= 0 && index < len) {
            Array.set(target, index, value);
        } else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            Object newArray = Array.newInstance(target.getClass().getComponentType(), index + 1);
            System.arraycopy(target, 0, newArray, 0, len);
            Array.set(newArray, index, value);
            Node parent = ctx.getCurrentNode().jjtGetParent();
            Node pparent = parent.jjtGetParent();
            String parentExpr = pparent.toString();
            int lastTokenPos = parentExpr.lastIndexOf('[');
            parentExpr = parentExpr.substring(0, lastTokenPos);
            Ognl.setValue(parentExpr, ctx.getRoot(), newArray);
        }
    }
