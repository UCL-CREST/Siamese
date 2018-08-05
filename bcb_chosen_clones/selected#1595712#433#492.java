    private Object newInstanceGroupedAvp(Class<?> m, AvpDscr ad, Avp avp) throws RecoderException {
        Object rc;
        int cacount = 0;
        ClassInfo c = storage.getClassInfo(m);
        Constructor<?> cm = null;
        Map<String, Class<?>> cmargs = new HashMap<String, Class<?>>();
        for (ConstructorInfo ci : c.getConstructorsInfo()) {
            if (ci.getAnnotation(Setter.class) != null) {
                Class<?>[] params = ci.getConstructor().getParameterTypes();
                boolean correct = true;
                for (Class<?> j : params) {
                    if (j.isArray()) j = j.getComponentType();
                    if (storage.getClassInfo(j).getAnnotation(AvpDscr.class) == null) {
                        correct = false;
                        break;
                    }
                }
                if (!correct) continue;
                if (cacount < params.length) {
                    cacount = params.length;
                    cm = ci.getConstructor();
                }
            }
        }
        try {
            List<Object> initargs = new ArrayList<Object>();
            if (cm != null) {
                for (Class<?> ac : cm.getParameterTypes()) {
                    Class<?> lac = ac.isArray() ? ac.getComponentType() : ac;
                    cmargs.put(lac.getName(), ac);
                    initargs.add(createChildByAvp(findChildDscr(ad.childs(), ac), ac, avp.getGrouped()));
                }
                rc = cm.newInstance(initargs.toArray());
            } else {
                rc = m.newInstance();
            }
            for (MethodInfo mi : c.getMethodsInfo()) {
                if (mi.getAnnotation(Setter.class) != null) {
                    Class<?>[] pt = mi.getMethod().getParameterTypes();
                    if (pt.length == 1 && storage.getClassInfo(pt[0]).getAnnotation(AvpDscr.class) != null) {
                        Class<?> ptc = pt[0].isArray() ? pt[0].getComponentType() : pt[0];
                        if (!cmargs.containsKey(ptc.getName())) {
                            cmargs.put(ptc.getName(), ptc);
                            mi.getMethod().invoke(rc, createChildByAvp(findChildDscr(ad.childs(), pt[0]), pt[0], avp.getGrouped()));
                        }
                    }
                }
            }
            setUndefinedAvp(avp.getGrouped(), rc, c, cmargs);
        } catch (InstantiationException e) {
            throw new RecoderException(e);
        } catch (InvocationTargetException e) {
            throw new RecoderException(e);
        } catch (AvpDataException e) {
            throw new RecoderException(e);
        } catch (IllegalAccessException e) {
            throw new RecoderException(e);
        }
        return rc;
    }
