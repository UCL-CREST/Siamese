    public L2Effect getStolenEffect(Env env, L2Effect stolen) {
        Class<?> func;
        Constructor<?> stolenCons;
        try {
            func = Class.forName("com.l2jserver.gameserver.skills.effects.Effect" + stolen.getEffectTemplate().funcName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            stolenCons = func.getConstructor(Env.class, L2Effect.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            L2Effect effect = (L2Effect) stolenCons.newInstance(env, stolen);
            return effect;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            _log.log(Level.WARNING, "Error creating new instance of Class " + func + " Exception was: " + e.getTargetException().getMessage(), e.getTargetException());
            return null;
        }
    }
