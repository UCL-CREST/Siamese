    public Boolean accumulate(final Object[] r, final List bindings) {
        Object arg = r[0];
        if (arg instanceof ConstantTerm) {
            ConstantTerm t = (ConstantTerm) arg;
            add(t.getObject());
        } else if (arg instanceof ComplexTerm) {
            ComplexTerm ct = (ComplexTerm) arg;
            Object first = ct.getTerms()[1];
            if (first instanceof ConstantTerm) {
                try {
                    String constructor = ((ConstantTerm) first).getObject().toString();
                    Class cl = Class.forName(constructor);
                    List list = new ArrayList();
                    RListUtils.rlist2List((ComplexTerm) ct.getTerms()[0], list, 0);
                    int arity = list.size();
                    Object[] dat = new Object[arity];
                    Class[] par = new Class[arity];
                    int i = 0;
                    for (ListIterator li = list.listIterator(); li.hasNext(); i++) {
                        Object o = li.next();
                        dat[i] = o;
                        par[i] = o.getClass();
                        if (par[i] == Double.class) {
                            par[i] = double.class;
                        } else if (par[i] == Boolean.class) {
                            par[i] = boolean.class;
                        } else if (par[i] == Integer.class) {
                            par[i] = int.class;
                        } else if (o instanceof List) {
                            par[i] = List.class;
                        } else if (o instanceof ComplexTerm) {
                            par[i] = Object.class;
                        }
                    }
                    Object target = null;
                    Object o = cl.getConstructor(par).newInstance(dat);
                    add(o);
                    return Boolean.FALSE;
                } catch (Exception ex) {
                }
            }
            add(arg);
        } else {
            add(arg);
        }
        return Boolean.FALSE;
    }
