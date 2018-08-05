    @SuppressWarnings({ "unchecked" })
    public static Object getInstance(Class daoClass, Class entityClass, SessionFactory sessionFactory) {
        log.info("Creating implementation of " + daoClass.getName());
        log.info("Entity class name: " + entityClass.getName());
        if (daoClass == null || entityClass == null) {
            throw new IllegalArgumentException("Both parameters should not be null");
        }
        String implClassName = daoClass.getName() + "DynDaoImpl";
        String implClassShortName = daoClass.getSimpleName() + "DynDaoImpl";
        try {
            Class daoImpl = Class.forName(implClassName);
            log.info("Already implemented; returning instance of loaded class");
            return daoImpl.getConstructor(Class.class, SessionFactory.class).newInstance(entityClass, sessionFactory);
        } catch (ClassNotFoundException ex) {
            log.info("Creating implementation");
            try {
                ClassPool classPool = ClassPool.getDefault();
                classPool.insertClassPath(new ClassClassPath(daoClass));
                CtClass dao;
                if (daoClass.isInterface()) {
                    dao = classPool.makeClass(implClassName);
                    dao.addInterface(classPool.get(daoClass.getName()));
                } else {
                    CtClass parent = classPool.get(daoClass.getName());
                    dao = classPool.makeClass(implClassName, parent);
                }
                classPool.importPackage("org.hibernate");
                classPool.importPackage("org.hibernate.criterion");
                classPool.importPackage("org.hibernate.metadata");
                classPool.importPackage("ru.maxmatveev.dyndao");
                classPool.importPackage("ru.maxmatveev.dyndao.impl.hibernate");
                classPool.importPackage("java.io");
                classPool.importPackage("java.util");
                dao.addField(CtField.make("private final Class entityClass;", dao));
                dao.addField(CtField.make("private final SessionFactory sessionFactory;", dao));
                dao.addField(CtField.make("private String id;", dao));
                dao.addField(CtField.make("private String temp;", dao));
                dao.addField(CtField.make("private ThreadLocal aliases = new ThreadLocal();", dao));
                dao.addField(CtField.make("private String idName = null;", dao));
                dao.addConstructor(CtNewConstructor.make(String.format(DEFAULT_CONSTRUCTOR, implClassShortName), dao));
                dao.addConstructor(CtNewConstructor.make(String.format(CONSTRUCTOR, implClassShortName), dao));
                dao.addMethod(CtNewMethod.make(DELETE, dao));
                dao.addMethod(CtNewMethod.make(MERGE, dao));
                dao.addMethod(CtNewMethod.make(GET_BY_ID, dao));
                dao.addMethod(CtNewMethod.make(GET_ALL, dao));
                dao.addMethod(CtNewMethod.make(SAVE, dao));
                dao.addMethod(CtNewMethod.make(CREATE_ALIAS, dao));
                dao.addMethod(CtNewMethod.make(CREATE_ALIAS_DT, dao));
                dao.addMethod(CtNewMethod.make(GET_CRITERION, dao));
                dao.addMethod(CtNewMethod.make(GET_CRITERION_DT, dao));
                dao.addMethod(CtNewMethod.make(GET_SESSION_FACTORY, dao));
                for (CtMethod method : dao.getMethods()) {
                    if (Modifier.isAbstract(method.getModifiers()) && isDynDaoMethod(method)) {
                        log.info("Implementing method: " + method.getName());
                        String modifiers = Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT);
                        String retType = method.getReturnType().getName();
                        String name = method.getName();
                        String params;
                        StringBuilder sb = new StringBuilder();
                        int idx = 0;
                        for (CtClass param : method.getParameterTypes()) {
                            if (idx != 0) {
                                sb.append(", ");
                            }
                            sb.append(String.format("%s p%d", param.getName(), idx++));
                        }
                        params = sb.toString();
                        String exceptions;
                        sb = new StringBuilder();
                        idx = 0;
                        for (CtClass e : method.getExceptionTypes()) {
                            if (idx != 0) {
                                sb.append(", ");
                            } else {
                                sb.append("throws ");
                            }
                            idx++;
                            sb.append(e.getName());
                        }
                        exceptions = sb.toString();
                        StringBuilder impl = new StringBuilder();
                        impl.append(GET_SESSION);
                        impl.append("aliases.set(new HashSet());");
                        boolean count = (getCountAnnotation(method) != null);
                        Hql hql = getHqlAnnotation(method);
                        if (count && hql != null) {
                            throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                        }
                        boolean byParent = false;
                        boolean restrict = false;
                        boolean sorting = false;
                        boolean pagination = false;
                        List<Integer> parentRestrictionIndex = new ArrayList<Integer>();
                        List<Restriction.Parent> parentRestriction = new ArrayList<Restriction.Parent>();
                        List<Integer> parentFieldRestrictionIndex = new ArrayList<Integer>();
                        List<Restriction.ParentField> parentFieldRestriction = new ArrayList<Restriction.ParentField>();
                        List<Integer> parentIdRestrictionIndex = new ArrayList<Integer>();
                        List<Restriction.ParentId> parentIdRestriction = new ArrayList<Restriction.ParentId>();
                        List<Integer> restrictionIndex = new ArrayList<Integer>();
                        List<Restriction.Restrict> restriction = new ArrayList<Restriction.Restrict>();
                        List<Integer> restrictionFieldIndex = new ArrayList<Integer>();
                        List<Restriction.Field> restrictionField = new ArrayList<Restriction.Field>();
                        List<Integer> sortingCriteriaIndex = new ArrayList<Integer>();
                        List<Ordering.Criteria> sortingCriteria = new ArrayList<Ordering.Criteria>();
                        List<Ordering.Field> sortingField = new ArrayList<Ordering.Field>();
                        int paginationIndex = -1;
                        if (getOrderingFieldAnnotation(method) != null) {
                            sortingField.add(getOrderingFieldAnnotation(method));
                            if (count) {
                                throw new IllegalArgumentException("Sorting should not be used with @Count annotated methods");
                            }
                            if (hql != null) {
                                throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                            }
                            sorting = true;
                        }
                        CtClass[] arguments = method.getParameterTypes();
                        for (int i = 0; i < method.getParameterAnnotations().length; i++) {
                            Annotation annotation;
                            if ((annotation = getParamtererAnnotation(method.getParameterAnnotations()[i])) != null) {
                                if (annotation.annotationType() == Restriction.Parent.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    if (!arguments[i].subtypeOf(classPool.get(Restriction.class.getName()))) {
                                        throw new IllegalArgumentException("Annotation @Restriction.Parent should be used only on Resctriction type parameters.");
                                    }
                                    parentRestriction.add((Restriction.Parent) annotation);
                                    parentRestrictionIndex.add(i);
                                    byParent = true;
                                } else if (annotation.annotationType() == Restriction.ParentField.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    parentFieldRestriction.add((Restriction.ParentField) annotation);
                                    parentFieldRestrictionIndex.add(i);
                                    byParent = true;
                                } else if (annotation.annotationType() == Restriction.ParentId.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    if (!arguments[i].subtypeOf(classPool.get("java.io.Serializable"))) {
                                        throw new IllegalArgumentException("Annotation @Restriction.ParentId should be used only on Serializable type parameters.");
                                    }
                                    parentIdRestriction.add((Restriction.ParentId) annotation);
                                    parentIdRestrictionIndex.add(i);
                                    byParent = true;
                                } else if (annotation.annotationType() == Restriction.Restrict.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    if (!arguments[i].subtypeOf(classPool.get(Restriction.class.getName()))) {
                                        throw new IllegalArgumentException("Annotation @Restriction.Restrict should be used only on Resctriction type parameters.");
                                    }
                                    restriction.add((Restriction.Restrict) annotation);
                                    restrictionIndex.add(i);
                                    restrict = true;
                                } else if (annotation.annotationType() == Restriction.Field.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    restrictionField.add((Restriction.Field) annotation);
                                    restrictionFieldIndex.add(i);
                                    restrict = true;
                                } else if (annotation.annotationType() == Ordering.Criteria.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    if (count) {
                                        throw new IllegalArgumentException("Sorting should not be used with @Count annotated methods");
                                    }
                                    if (!arguments[i].subtypeOf(classPool.get(Ordering.class.getName()))) {
                                        throw new IllegalArgumentException("Annotation @Ordering.Criteria should be used only on Ordering type parameters.");
                                    }
                                    sortingCriteria.add((Ordering.Criteria) annotation);
                                    sortingCriteriaIndex.add(i);
                                    sorting = true;
                                } else if (annotation.annotationType() == Pagination.Paginate.class) {
                                    if (hql != null) {
                                        throw new IllegalArgumentException("Hql annotation cannot be used in one method with other DynDao annotations");
                                    }
                                    if (pagination) {
                                        throw new IllegalArgumentException("There is more than one pagination argument in method definition.");
                                    }
                                    if (count) {
                                        throw new IllegalArgumentException("Pagination should not be used with @Count annotated methods");
                                    }
                                    if (!arguments[i].subtypeOf(classPool.get(Pagination.class.getName()))) {
                                        throw new IllegalArgumentException("Annotation @Pagination.Paginate should be used only on Pagination type parameters.");
                                    }
                                    paginationIndex = i;
                                    pagination = true;
                                }
                            }
                        }
                        if (hql != null) {
                            impl.append(String.format(HQL_1, hql.query(), hql.first(), hql.max()));
                            for (int i = 0; i < method.getParameterTypes().length; i++) {
                                Named named = null;
                                for (int j = 0; j < method.getParameterAnnotations()[i].length; j++) {
                                    if (method.getParameterAnnotations()[i][j] instanceof Named) {
                                        named = (Named) method.getParameterAnnotations()[i][j];
                                    }
                                }
                                if (named != null) {
                                    if (Collection.class.isAssignableFrom(Class.forName(method.getParameterTypes()[i].getName()))) {
                                        impl.append(String.format(HQL_NAMED_COLLECTION_PARAMETER, named.value(), i + 1));
                                    } else {
                                        impl.append(String.format(HQL_NAMED_PARAMETER, named.value(), i + 1));
                                    }
                                } else {
                                    impl.append(String.format(HQL_PARAMETER, i, i + 1));
                                }
                            }
                            if (hql.query().toLowerCase().startsWith("update") || hql.query().toLowerCase().startsWith("delete")) {
                                impl.append(HQL_UPDATE);
                            } else {
                                if (method.getReturnType().subtypeOf(classPool.get("java.util.Collection"))) {
                                    impl.append(HQL_LIST);
                                } else {
                                    impl.append(HQL_UNIQUE);
                                }
                            }
                        } else if (byParent) {
                            impl.append(DEFINE_BY_PARENT_CRITERIAS);
                            for (Restriction.Parent aParentRestriction : parentRestriction) {
                                impl.append(String.format(PARENT_RESTRICTION_1, aParentRestriction.parentClass().getName(), aParentRestriction.parentClass().getName()));
                                for (Integer aParentRestrictionIndex : parentRestrictionIndex) {
                                    impl.append(String.format(PARENT_RESTRICTION_2, aParentRestrictionIndex + 1));
                                }
                                impl.append(String.format(PARENT_RESTRICTION_3, aParentRestriction.fieldName()));
                            }
                            for (int i = 0; i < parentFieldRestriction.size(); i++) {
                                impl.append(String.format(PARENT_RESTRICTION_1, parentFieldRestriction.get(i).parentClass().getName(), parentFieldRestriction.get(i).parentClass().getName()));
                                if (parentFieldRestriction.get(i).filterFieldComparison() == Restriction.Comparison.EQUALS) {
                                    impl.append(String.format(DT_RESTRICTION_EQ, parentFieldRestriction.get(i).filterFieldName(), parentFieldRestrictionIndex.get(i) + 1));
                                } else if (parentFieldRestriction.get(i).filterFieldComparison() == Restriction.Comparison.IEQUALS) {
                                    impl.append(String.format(DT_RESTRICTION_IEQ, parentFieldRestriction.get(i).filterFieldName(), parentFieldRestrictionIndex.get(i) + 1));
                                } else if (parentFieldRestriction.get(i).filterFieldComparison() == Restriction.Comparison.LIKE) {
                                    impl.append(String.format(DT_RESTRICTION_LIKE, parentFieldRestriction.get(i).filterFieldName(), parentFieldRestrictionIndex.get(i) + 1));
                                } else if (parentFieldRestriction.get(i).filterFieldComparison() == Restriction.Comparison.ILIKE) {
                                    impl.append(String.format(DT_RESTRICTION_ILIKE, parentFieldRestriction.get(i).filterFieldName(), parentFieldRestrictionIndex.get(i) + 1));
                                }
                                impl.append(String.format(PARENT_RESTRICTION_3, parentFieldRestriction.get(i).fieldName()));
                            }
                            for (int i = 0; i < parentIdRestriction.size(); i++) {
                                impl.append(String.format(PARENT_ID_RESTRICTION, parentIdRestriction.get(i).parentClass().getName(), parentIdRestrictionIndex.get(i) + 1, parentIdRestriction.get(i).parentClass().getName(), parentIdRestriction.get(i).fieldName()));
                            }
                            if (restrict) {
                                for (int i = 0; i < restrictionField.size(); i++) {
                                    if (restrictionField.get(i).comparison() == Restriction.Comparison.EQUALS) {
                                        impl.append(String.format(DT_RESTRICTION_EQ, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    } else if (restrictionField.get(i).comparison() == Restriction.Comparison.IEQUALS) {
                                        impl.append(String.format(DT_RESTRICTION_IEQ, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    } else if (restrictionField.get(i).comparison() == Restriction.Comparison.LIKE) {
                                        impl.append(String.format(DT_RESTRICTION_LIKE, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    } else if (restrictionField.get(i).comparison() == Restriction.Comparison.ILIKE) {
                                        impl.append(String.format(DT_RESTRICTION_ILIKE, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    }
                                }
                                for (int i = 0; i < restriction.size(); i++) {
                                    impl.append(String.format(RESTRICTION_WITH_PARENT, restrictionIndex.get(i) + 1));
                                }
                            }
                            if (count) {
                                impl.append(RETURN_COUNT_WITH_PARENT);
                            } else {
                                impl.append(BY_PARENT_CRITERIA);
                                if (sorting) {
                                    for (Ordering.Field aSortingField : sortingField) {
                                        if (aSortingField.direction() == Ordering.Direction.ASC) {
                                            impl.append(String.format(ORDER_ASC, aSortingField.fieldName()));
                                        } else if (aSortingField.direction() == Ordering.Direction.DESC) {
                                            impl.append(String.format(ORDER_DESC, aSortingField.fieldName()));
                                        }
                                    }
                                    for (Integer aSortingCriteriaIndex : sortingCriteriaIndex) {
                                        impl.append(String.format(ORDER_CRITERIA, aSortingCriteriaIndex + 1));
                                    }
                                }
                                if (pagination) {
                                    if (paginationIndex != -1) {
                                        impl.append(String.format(PAGINATION, paginationIndex + 1, paginationIndex + 1, paginationIndex + 1));
                                    }
                                }
                                if (count) {
                                    impl.append(RETURN_COUNT);
                                } else {
                                    if (method.getReturnType().subtypeOf(classPool.get("java.util.Collection"))) {
                                        impl.append(RETURN_LIST);
                                    } else {
                                        impl.append(RETURN_UNIQUE);
                                    }
                                }
                            }
                        } else {
                            impl.append(CREATE_CRITERIA);
                            if (restrict) {
                                for (int i = 0; i < restrictionField.size(); i++) {
                                    if (restrictionField.get(i).comparison() == Restriction.Comparison.EQUALS) {
                                        impl.append(String.format(RESTRICT_EQ, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1, restrictionFieldIndex.get(i) + 1));
                                    } else if (restrictionField.get(i).comparison() == Restriction.Comparison.IEQUALS) {
                                        impl.append(String.format(RESTRICT_IEQ, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    } else if (restrictionField.get(i).comparison() == Restriction.Comparison.LIKE) {
                                        impl.append(String.format(RESTRICT_LIKE, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    } else if (restrictionField.get(i).comparison() == Restriction.Comparison.ILIKE) {
                                        impl.append(String.format(RESTRICT_ILIKE, restrictionField.get(i).fieldName(), restrictionFieldIndex.get(i) + 1));
                                    }
                                }
                                for (Integer aRestrictionIndex : restrictionIndex) {
                                    impl.append(String.format(RESTRICT_CRITERIA, aRestrictionIndex + 1));
                                }
                            }
                            if (sorting) {
                                for (Ordering.Field aSortingField : sortingField) {
                                    if (aSortingField.direction() == Ordering.Direction.ASC) {
                                        impl.append(String.format(ORDER_ASC, aSortingField.fieldName()));
                                    } else if (aSortingField.direction() == Ordering.Direction.DESC) {
                                        impl.append(String.format(ORDER_DESC, aSortingField.fieldName()));
                                    }
                                }
                                for (Integer aSortingCriteriaIndex : sortingCriteriaIndex) {
                                    impl.append(String.format(ORDER_CRITERIA, aSortingCriteriaIndex + 1));
                                }
                            }
                            if (pagination) {
                                if (paginationIndex != -1) {
                                    impl.append(String.format(PAGINATION, paginationIndex + 1, paginationIndex + 1, paginationIndex + 1));
                                }
                            }
                            if (count) {
                                impl.append(RETURN_COUNT);
                            } else {
                                if (method.getReturnType().subtypeOf(classPool.get("java.util.Collection"))) {
                                    impl.append(RETURN_LIST);
                                } else {
                                    impl.append(RETURN_UNIQUE);
                                }
                            }
                        }
                        impl.append("}");
                        String implementation = new StringBuffer().append(modifiers).append(" ").append(retType).append(" ").append(name).append("(").append(params).append(") ").append(exceptions).append(" ").append(impl).toString();
                        log.debug(implementation);
                        CtMethod implementedMethod = CtNewMethod.make(implementation, dao);
                        dao.addMethod(implementedMethod);
                    }
                }
                log.info("Loading generated class");
                Class daoImpl = dao.toClass(entityClass.getClassLoader(), null);
                log.info("Detaching");
                dao.detach();
                log.info("Returning new instance");
                return daoImpl.getConstructor(Class.class, SessionFactory.class).newInstance(entityClass, sessionFactory);
            } catch (NotFoundException e) {
                throw new IllegalArgumentException("Super interface/class for DAO is not found.", e);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot create new DAO class.", e);
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot instantiate existing DAO class.", ex);
        }
    }
