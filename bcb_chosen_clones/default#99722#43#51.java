    public void run() {
        boolean proceed = true;
        while (proceed && workflow.hasNext()) {
            api = (ArrayList) workflow.next();
            workflow.remove();
            Method meth = capiClass.getMethod(api.get(1), apiClass);
            proceed = (boolean) meth.invoke(capi, api);
        }
    }
