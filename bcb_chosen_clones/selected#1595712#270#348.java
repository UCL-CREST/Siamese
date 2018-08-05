    public <T> T decode(Message message, java.lang.Class<T> yourDomainMessageObject) throws RecoderException {
        Object rc = null;
        ClassInfo c = storage.getClassInfo(yourDomainMessageObject);
        CommandDscr cd = c.getAnnotation(CommandDscr.class);
        if (cd != null) {
            try {
                if (message.getCommandCode() != cd.code()) throw new IllegalArgumentException("Invalid message code " + message.getCommandCode());
                if (message.getApplicationId() != 0 && message.getApplicationId() != cd.appId()) throw new IllegalArgumentException("Invalid Application-Id " + message.getApplicationId());
                for (CommandFlag f : cd.flags()) {
                    switch(f) {
                        case E:
                            if (!message.isError()) throw new IllegalArgumentException("Flag e is not set");
                            break;
                        case P:
                            if (!message.isProxiable()) throw new IllegalArgumentException("Flag p is not set");
                            break;
                        case R:
                            if (!message.isRequest()) throw new IllegalArgumentException("Flag m is not set");
                            break;
                        case T:
                            if (!message.isReTransmitted()) throw new IllegalArgumentException("Flag t is not set");
                            break;
                    }
                }
                int cacount = 0;
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
                List<Object> initargs = new ArrayList<Object>();
                if (cm != null) {
                    for (Class<?> ac : cm.getParameterTypes()) {
                        Class<?> lac = ac.isArray() ? ac.getComponentType() : ac;
                        cmargs.put(lac.getName(), ac);
                        initargs.add(createChildByAvp(findChildDscr(cd.childs(), ac), ac, message.getAvps()));
                    }
                    rc = cm.newInstance(initargs.toArray());
                } else {
                    rc = yourDomainMessageObject.newInstance();
                }
                for (MethodInfo mi : c.getMethodsInfo()) {
                    if (mi.getAnnotation(Setter.class) != null) {
                        Class<?>[] pt = mi.getMethod().getParameterTypes();
                        if (pt.length == 1 && storage.getClassInfo(pt[0]).getAnnotation(AvpDscr.class) != null) {
                            Class<?> ptc = pt[0].isArray() ? pt[0].getComponentType() : pt[0];
                            if (!cmargs.containsKey(ptc.getName())) {
                                cmargs.put(ptc.getName(), ptc);
                                mi.getMethod().invoke(rc, createChildByAvp(findChildDscr(cd.childs(), pt[0]), pt[0], message.getAvps()));
                            }
                        }
                    }
                }
                setUndefinedAvp(message.getAvps(), rc, c, cmargs);
            } catch (InstantiationException e) {
                throw new RecoderException(e);
            } catch (InvocationTargetException e) {
                throw new RecoderException(e);
            } catch (IllegalAccessException e) {
                throw new RecoderException(e);
            }
        }
        return (T) rc;
    }
