    public Page compose(String target) throws Exception {
        return (Page) getConstructor(target).newInstance(new Object[] { context, target });
    }
