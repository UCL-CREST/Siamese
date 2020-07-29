    private Object newInstanceSimpleAvp(Class<?> m, AvpDscr ad, Avp avp) {
        Object rc = null;
        if (avp == null) return null;
        ClassInfo c = storage.getClassInfo(m);
        try {
            for (ConstructorInfo ci : c.getConstructorsInfo()) {
                if (ci.getConstructor().getParameterTypes().length == 1 && ci.getAnnotation(Setter.class) != null) {
                    List<Object> args = new ArrayList<Object>();
                    if (ci.getConstructor().getParameterTypes()[0].isArray()) {
                        args.add(getValue(ad.type(), avp));
                    } else {
                        args.add(getValue(ad.type(), avp));
                    }
                    rc = ci.getConstructor().newInstance(args.toArray());
                }
            }
            if (rc == null) {
                rc = m.newInstance();
                for (MethodInfo mi : c.getMethodsInfo()) {
                    if (mi.getAnnotation(Setter.class) != null) {
                        List<Object> args = new ArrayList<Object>();
                        if (mi.getMethod().getParameterTypes()[0].isArray()) {
                            args.add(getValue(ad.type(), avp));
                        } else {
                            args.add(getValue(ad.type(), avp));
                        }
                        mi.getMethod().invoke(rc, args);
                    }
                }
            }
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
