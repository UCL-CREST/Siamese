    private Object createObject(ParserXML.Tag tag, ParserXML.Attr[] addAttrTmp, Object parent) throws IOException, ClassNotFoundException {
        String elementName = tag.name;
        if (KELLY_DEBUG) System.err.println("adding " + elementName);
        Object me = parent;
        boolean nameMissing = false;
        NamedObject namedObject;
        if (elementName.equals(XMLSerialize.NULL_TOKEN)) {
            namedObject = deserializeNull(addAttrTmp, parent);
            me = namedObject.object;
            nameMissing = !namedObject.named;
            if (nameMissing) {
                if (parent != null) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                }
            }
            return me;
        } else if (elementName.startsWith(XMLSerialize.ARRAY_TOKEN)) {
            namedObject = createArray(addAttrTmp, elementName, parent);
            me = namedObject.object;
            nameMissing = !namedObject.named;
            if (nameMissing) {
                if (parent != null) {
                    if (parent != null && parent.getClass() != Hashtable.class && parent.getClass() != Vector.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                }
            }
            return me;
        } else if ((namedObject = getAlias(addAttrTmp, elementName, parent)) != null) {
            me = namedObject.object;
            nameMissing = !namedObject.named;
            if (nameMissing) {
                if (parent != null) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                }
            }
            return me;
        } else if (elementName.equals(XMLSerialize.BINARY_DATA_TOKEN)) {
            namedObject = deserializeBinary(addAttrTmp, tag, parent);
            nameMissing = !namedObject.named;
            me = namedObject.object;
            if (parent != null) {
                if (nameMissing) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                }
            }
            return me;
        } else if (elementName.equals("java.lang.String")) {
            namedObject = deserializeWrapper(addAttrTmp, tag, parent);
            nameMissing = !namedObject.named;
            me = namedObject.object;
            if (parent != null) {
                if (nameMissing) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                }
            }
            return me;
        } else if (elementName.equals("java.lang.Class")) {
            if (DEBUG_CLASS) System.err.println("Got a Class tag");
            namedObject = deserializeClass(addAttrTmp, tag, parent);
            nameMissing = !namedObject.named;
            me = namedObject.object;
            if (parent != null) {
                if (nameMissing) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                }
            }
            if (DEBUG_CLASS) {
                System.err.println(">>>>start>>>>>");
                System.err.println(me);
                try {
                    System.err.println(((Class) me).newInstance());
                    new JSX.ObjOut(System.err).writeObject(((Class) me).newInstance());
                } catch (InstantiationException e) {
                    System.err.println(e);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.err.println("Should never get here - it means that we couldn't create a Hashtable with the no-arg constructor");
                    e.printStackTrace();
                }
                System.err.println("<<<<end<<<<");
            }
            return me;
        } else if (elementName.equals("java.util.Vector")) {
            Class clazz = Class.forName(ParseUtilities.descapeDollar(elementName));
            me = null;
            try {
                me = clazz.newInstance();
            } catch (InstantiationException e) {
                if (DEBUG) {
                    System.err.println("Should never get here - it means that we couldn't create a Hashtable with the no-arg constructor");
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (DEBUG) {
                    System.err.println("Should never get here - it means that we couldn't create a Hashtable with the no-arg constructor");
                    e.printStackTrace();
                }
            }
            ParserXML.Attr name = null;
            name = addPrim(addAttrTmp, me, false);
            nameMissing = name.nameMissing;
            if (parent != null) {
                if (nameMissing) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                } else {
                    currentGetField.putObj(name.value, me);
                    Field f = getAllField(parent.getClass(), name.value);
                    setFinal(f, parent, me);
                }
            }
            putAlias(me);
            if (!name.emptyTag) {
                addVectorElements((Vector) me);
            }
            return me;
        } else if (elementName.equals("java.util.Hashtable")) {
            Class clazz = Class.forName(ParseUtilities.descapeDollar(elementName));
            me = null;
            try {
                me = clazz.newInstance();
            } catch (InstantiationException e) {
                if (DEBUG) {
                    System.err.println("Should never get here - it means that we couldn't create a Hashtable with the no-arg constructor");
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (DEBUG) {
                    System.err.println("Should never get here - it means that we couldn't create a Hashtable with the no-arg constructor");
                    e.printStackTrace();
                }
            }
            ParserXML.Attr name = null;
            name = addPrim(addAttrTmp, me, false);
            nameMissing = name.nameMissing;
            if (parent != null) {
                if (nameMissing) {
                    if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                        objStore.add(me);
                    }
                } else {
                    currentGetField.putObj(name.value, me);
                    Field f = getAllField(parent.getClass(), name.value);
                    setFinal(f, parent, me);
                }
            }
            putAlias(me);
            if (!name.emptyTag) {
                addHashtableElements((Hashtable) me);
            }
            return me;
        } else {
            ParserXML.Attr name = addAttrTmp[1];
            boolean subclass = false;
            if (name != null && name.value.equals(XMLSerialize.SUPER_TOKEN)) {
                subclass = true;
            }
            String alias = null;
            Class meClassFrame;
            if (!subclass) {
                Class clazz = null;
                String escapedName = ParseUtilities.descapeDollar(elementName);
                ObjectStreamClass osc = getOsc(escapedName);
                clazz = oisSubclass.resolveClass(osc);
                if (clazz == null) throw new ClassNotFoundException("Could not load " + escapedName);
                me = null;
                try {
                    if (Externalizable.class.isAssignableFrom(clazz)) {
                        try {
                            Constructor cons = clazz.getConstructor(new Class[0]);
                            me = cons.newInstance(new Object[0]);
                        } catch (NoSuchMethodException e) {
                            throw new InvalidClassException("Missing public no-arg constructor for class " + clazz.getName());
                        } catch (InstantiationException e) {
                            throw new InvalidClassException("An interface or abstract class " + clazz.getName());
                        }
                    } else {
                        me = MagicClass.newInstance(clazz);
                        if (me == null) throw new ClassNotFoundException("MagicClass.newInstance() failed to create a " + clazz + ", from the name " + escapedName);
                    }
                } catch (InvocationTargetException e) {
                    Throwable thrown = e.getTargetException();
                    if (thrown instanceof ClassNotFoundException) throw (ClassNotFoundException) thrown; else if (thrown instanceof IOException) throw (IOException) thrown; else if (thrown instanceof RuntimeException) throw (RuntimeException) thrown; else {
                        System.err.println("\nWrapper Exception:");
                        e.printStackTrace();
                        System.err.println("\nException wrapped up:");
                        thrown.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (IBM_DEBUG) System.err.println("IBM: me is " + (me == null ? "null" : "not null"));
                if (KELLY_DEBUG) {
                    if (me.getClass() == Class.forName("java.awt.Container")) {
                        System.err.println("Got a java.awt.Container");
                    }
                }
                if (DEBUG) System.err.println("Made class \"" + elementName + "\"");
                alias = putAlias(me);
                meClassFrame = clazz;
                if (superVersion) {
                    lookahead.set(tag, addAttrTmp);
                    f(me);
                }
            } else {
                meClassFrame = Class.forName(ParseUtilities.descapeDollar(elementName));
            }
            if (!superVersion) {
                Vector localStackPrimStore = primStore;
                int localStackPrimStoreIndex = primStoreIndex;
                primStore = new Vector();
                primStoreIndex = 0;
                name = addPrim(addAttrTmp, me, false);
                nameMissing = name.nameMissing;
                if (DEBUG && name != null) System.err.println("Name: " + name);
                if (DEBUG) System.err.println("nameMissing: " + nameMissing);
                if (INTERNAL_DEBUG) {
                    if (primStore != null) {
                        for (Enumeration keys = primStore.elements(); keys.hasMoreElements(); ) {
                            Object a = keys.nextElement();
                            System.err.println("In primStore: " + a + " -- " + a.getClass() + " -- " + a.hashCode());
                        }
                    }
                }
                Vector localStackObjStore = objStore;
                objStore = new Vector();
                int localStackObjStoreIndex = objStoreIndex;
                objStoreIndex = 0;
                Object localMeStore = meStore;
                meStore = me;
                Class localMeClassFrameStore = meClassFrameStore;
                meClassFrameStore = meClassFrame;
                boolean localEmptyTagStore = emptyTagStore;
                emptyTagStore = name.emptyTag;
                boolean localDefaultCalledStore = defaultCalledStore;
                defaultCalledStore = false;
                Method m;
                if (me instanceof Externalizable) {
                    ((Externalizable) me).readExternal(oisSubclass);
                } else {
                    Vector classes = new Vector();
                    Class[] superClasses = XMLSerialize.getReversedSuperClasses(me.getClass());
                    if (SUPER_DEBUG) System.err.println(me.getClass().getName() + " " + (!nameMissing ? name.value : "<unnamed>") + ": superClasses.length = " + superClasses.length);
                    for (int j = 0; j < superClasses.length; j++) {
                        Class c = superClasses[j];
                        if ((m = XMLSerialize.getDeclaredMethod(c, "readObject", OIS_ARGS, Modifier.PRIVATE, Modifier.STATIC)) != null) {
                            if (INTERNAL_DEBUG) System.err.print("\t--JSX deserialize--");
                            try {
                                m.invoke(me, readObjectArglist);
                                if (INTERNAL_DEBUG) System.err.println("\tinvoke " + m + " on " + me + " of " + me.getClass());
                            } catch (InvocationTargetException e) {
                                if (DEBUG) {
                                    System.err.println("JSX InvocationTargetException:");
                                    System.err.println("Object which is a: " + me.getClass());
                                    e.printStackTrace();
                                }
                                Throwable t = e.getTargetException();
                                if (t instanceof ClassNotFoundException) throw (ClassNotFoundException) t; else if (t instanceof IOException) throw (IOException) t; else if (t instanceof RuntimeException) throw (RuntimeException) t; else if (t instanceof Error) throw (Error) t; else throw new Error("interal error");
                            } catch (IllegalAccessException e) {
                                defaultReadObject();
                            }
                        } else {
                            defaultReadObject();
                        }
                    }
                    if (superClasses.length == 0) defaultReadObject();
                }
                if (!subclass) {
                    if (NOV_DEBUG) System.err.println("readResolve is called!!!!!!!!!!!");
                    me = readResolve(me, alias);
                }
                defaultCalledStore = localDefaultCalledStore;
                meStore = localMeStore;
                meClassFrameStore = localMeClassFrameStore;
                emptyTagStore = localEmptyTagStore;
                objStoreIndex = localStackObjStoreIndex;
                objStore = localStackObjStore;
                primStoreIndex = localStackPrimStoreIndex;
                primStore = localStackPrimStore;
            }
            if (superVersion) {
                if (!subclass) {
                    if (NOV_DEBUG) System.err.println("readResolve is called!!!!!!!!!!!");
                    me = readResolve(me, alias);
                }
            }
            if (!subclass) {
                if (parent != null) {
                    if (nameMissing) {
                        if (parent.getClass() != Vector.class && parent.getClass() != Hashtable.class && !parent.getClass().isArray()) {
                            objStore.add(me);
                            if (INTERNAL_DEBUG) System.err.println("custom obj[" + objStoreIndex + "]," + objStore);
                        }
                    } else {
                        if (DEBUG_CUSTOM) {
                            System.err.println("getting field '" + name.value + "'");
                        }
                        currentGetField.putObj(name.value, me);
                        Field f = getAllField(parent.getClass(), name.value);
                        setFinal(f, parent, me);
                    }
                }
            }
            return me;
        }
    }
