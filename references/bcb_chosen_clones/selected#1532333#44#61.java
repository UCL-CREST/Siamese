    public Object init(MacroContext resources) {
        analyse();
        try {
            Macro meta = clazz.getAnnotation(Macro.class);
            String init = meta == null ? "init" : meta.init();
            if (init != null) {
                if ("<init>".equals(init)) return clazz.getConstructor(MacroContext.class).newInstance(resources);
                Object result = clazz.newInstance();
                if (init.length() > 0) clazz.getMethod(init, MacroContext.class).invoke(result, resources);
                return result;
            }
            return clazz.newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
