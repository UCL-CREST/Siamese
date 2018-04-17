    @SuppressWarnings("unchecked")
    public Object invokeConstructor(String $1, AValue... $2) {
        try {
            if ($2 != null) {
                LinkedList<Class> classes = new LinkedList<Class>();
                for (AValue type : $2) {
                    classes.add(ClassType.forgeClass(type.getType()));
                }
                Class clazz = Class.forName($1);
                Constructor ctr = clazz.getConstructor(classes.toArray(new Class[] {}));
                LinkedList<Object> values = new LinkedList<Object>();
                for (AValue arg : $2) {
                    values.add(arg.getValue());
                }
                return ctr.newInstance(values.toArray());
            } else {
                Class clazz = Class.forName($1);
                Constructor ctr = clazz.getConstructor(new Class[] {});
                return ctr.newInstance(new Object[] {});
            }
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        try {
            if ($2 != null) {
                LinkedList<Class> classes = new LinkedList<Class>();
                for (AValue type : $2) {
                    classes.add(ClassType.forgeClass(type.getType()));
                }
                Class clazz = Class.forName($1);
                Constructor ctr = clazz.getDeclaredConstructor(classes.toArray(new Class[] {}));
                ctr.setAccessible(true);
                LinkedList<Object> values = new LinkedList<Object>();
                for (AValue arg : $2) {
                    values.add(arg.getValue());
                }
                return ctr.newInstance(values.toArray());
            } else {
                Class clazz = Class.forName($1);
                Constructor ctr = clazz.getDeclaredConstructor(new Class[] {});
                ctr.setAccessible(true);
                return ctr.newInstance(new Object[] {});
            }
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        if ($2.length == 0) return null;
        Class clazz;
        try {
            clazz = Class.forName($1);
            LinkedList objects = new LinkedList();
            for (AValue value : $2) {
                objects.add(value.getValue());
            }
            LinkedList<Constructor> ctrs = new LinkedList<Constructor>();
            for (Constructor ctr : clazz.getConstructors()) {
                if (ctr.getName().equals($1) && ctr.getParameterTypes().length == $2.length) {
                    ctrs.add(ctr);
                }
            }
            for (Constructor ctr : clazz.getDeclaredConstructors()) {
                if (ctr.getName().equals($1) && ctr.getParameterTypes().length == $2.length) {
                    if (!ctrs.contains(ctr)) ctrs.add(ctr);
                }
            }
            LinkedList<Constructor> foundSubs = new LinkedList<Constructor>();
            for (Constructor ctr : ctrs) {
                boolean check = true;
                Class[] localParams = ctr.getParameterTypes();
                for (int index = 0; index < localParams.length; index++) {
                    check = check && localParams[index].isInstance(objects.get(index));
                    if (!check && localParams[index].getSuperclass() != null) check = !check && localParams[index].getSuperclass().isInstance($2[index].getValue());
                }
                if (check) foundSubs.add(ctr);
            }
            if (foundSubs.size() > 0) {
                return foundSubs.getFirst().newInstance(objects.toArray());
            }
        } catch (ClassNotFoundException e) {
        } catch (IllegalArgumentException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }
