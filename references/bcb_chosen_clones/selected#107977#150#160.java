    public CustomArticle NewInstance(NpsContext ctxt, Topic top, ResultSet rs) throws Exception {
        if (top == null) throw new NpsException(ErrorHelper.SYS_NOTOPIC);
        if (top.GetTable() == null || top.GetTable().length() == 0) throw new NpsException(ErrorHelper.SYS_NEED_CUSTOM_TOPIC);
        String table_name = top.GetTable().toUpperCase();
        if (classes == null || classes.isEmpty() || !classes.containsKey(table_name)) {
            return new CustomArticle(ctxt, top, rs);
        }
        Class clazz = GetArticleClass(table_name);
        java.lang.reflect.Constructor aconstructor = clazz.getConstructor(new Class[] { NpsContext.class, Topic.class, ResultSet.class });
        return (CustomArticle) aconstructor.newInstance(new Object[] { ctxt, top, rs });
    }
