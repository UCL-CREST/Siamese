    public void workflowEngine() {
        boolean proceed = true;
        try {
            while (proceed && workflow.hasNext()) {
                Object api[] = new Object[1];
                api[0] = workflow.next();
                ArrayList paramList = (ArrayList) api[0];
                workflow.remove();
                Method meth = thisClass.getMethod((String) paramList.get(0), paramClass);
                Object o = meth.invoke(this, api);
                proceed = ((Boolean) o).booleanValue();
            }
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
    }
