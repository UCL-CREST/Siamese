    public List<ErrorReport> check(WorkflowTask pc, int position) throws PluginConfigurationException {
        LOG.debug("Checking plugin configuration: " + pc.getPluginDescription().getName());
        List<ErrorReport> allErrors = new ArrayList<ErrorReport>();
        Object[] inputs;
        try {
            inputs = WorkflowDescriptionConf.instance().parseArgumentsForChecking(pc);
        } catch (Exception e1) {
            LOG.warn("Added exception to error report", e1);
            allErrors.add(new ErrorReport(e1.getMessage(), "Scope: plugin", pc.getPluginDescription().getName()));
            return allErrors;
        }
        try {
            Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(pc.getPluginDescription().getCls());
            if (ONDEXPlugin.class.isAssignableFrom(cls)) {
                try {
                    Constructor<ONDEXPlugin> con = (Constructor<ONDEXPlugin>) cls.getConstructor();
                    ONDEXPlugin p = (ONDEXPlugin) con.newInstance();
                    String[] validators = p.requiresValidators();
                    if (validators != null) {
                        for (String v : validators) {
                            String className = "net.sourceforge.ondex.validator." + v.toLowerCase() + ".Validator";
                            if (ValidatorRegistry.validators.keySet().contains(className)) {
                                continue;
                            }
                            try {
                                Thread.currentThread().getContextClassLoader().loadClass(className);
                            } catch (ClassNotFoundException e) {
                                allErrors.add(new ErrorReport("Required validator \"" + v + "\" is missing. Make sure all of the required dependancies are on the classpath and necessary data is deployed to data/importdata subfolders and restart the program to correct this error.", "", pc.getPluginDescription().getName()));
                            }
                        }
                    }
                } catch (NoSuchMethodException e) {
                    throw new PluginConfigurationException(e);
                } catch (InvocationTargetException e) {
                    throw new PluginConfigurationException(e);
                } catch (InstantiationException e) {
                    throw new PluginConfigurationException(e);
                } catch (IllegalAccessException e) {
                    throw new PluginConfigurationException(e);
                }
            }
        } catch (ClassNotFoundException e) {
            allErrors.add(new ErrorReport("Could not resolve plugin \"" + pc.getPluginDescription().getName() + "\". Make sure all of the required dependancies are on the classpath and restart the program to correct this error.", "", pc.getPluginDescription().getName()));
        }
        Set<ArgumentDescription> abs = new HashSet<ArgumentDescription>();
        for (BoundArgumentValue ap : pc.getArgs()) {
            ArgumentDescription ab = ap.getArg();
            abs.add(ab);
            if (ab.getParser() == null || ab.getParser().equals("standartArgument") || ab.getParser().equals("standart")) {
                if (ab.getIsRequired() && (ap.getValue() == null || ap.getValue().trim().length() == 0)) {
                    allErrors.add(new ErrorReport("The required argument was not supplied for " + ab.getName(), ab.getInteranlName(), pc.getPluginDescription().getName()));
                } else if (ab.isInputObject() || ab.isOutputObject()) {
                } else if (ap.getValue() != null && ab.getContentHint() != null) {
                    if (ab.getContentHint().startsWith("browse_folder")) {
                        File f = new File(ap.getValue());
                        if (!f.exists()) {
                            allErrors.add(new ErrorReport("Incorrect directory path specified for " + ab.getName(), ab.getInteranlName(), pc.getPluginDescription().getName()));
                        } else if (!f.isDirectory()) {
                            allErrors.add(new ErrorReport("Incorrect directory path specified for " + ab.getName() + " (is not a directory).", ab.getInteranlName(), pc.getPluginDescription().getName()));
                        }
                    } else if (ab.getContentHint().startsWith("browse_file")) {
                        File f = new File(ap.getValue());
                        if (!f.exists()) {
                            String path = ap.getValue();
                            int index = path.lastIndexOf(File.separator);
                            if (index < 0) {
                                index = path.lastIndexOf("/");
                                if (index < 0) {
                                    index = path.lastIndexOf("\\");
                                }
                            }
                            if (index > 0) {
                                String z = path.substring(0, index);
                                System.err.println(z);
                                File dir = new File(z);
                                if (!dir.exists()) {
                                    allErrors.add(new ErrorReport("Wrong directory name specified for " + ab.getName(), ab.getInteranlName(), pc.getPluginDescription().getName()));
                                } else if (!dir.isDirectory()) {
                                    allErrors.add(new ErrorReport("Wrong directory name specified for " + ab.getName(), ab.getInteranlName(), pc.getPluginDescription().getName()));
                                }
                            }
                        }
                    }
                }
            }
        }
        for (ArgumentDescription ab : pc.getPluginDescription().getArgDef()) {
            if (!abs.contains(ab)) {
                if (ab.getIsRequired()) {
                    allErrors.add(new ErrorReport("The required argument was not supplied for " + ab.getName(), ab.getInteranlName(), pc.getPluginDescription().getName()));
                }
            }
        }
        for (Object input : inputs) {
            if (input instanceof PluginAndArgs) {
                List<ErrorReport> errors = checkOndexArgs((PluginAndArgs) input);
                allErrors.addAll(errors);
            }
        }
        for (ErrorReport r : allErrors) {
            r.setPosition(position);
        }
        return allErrors;
    }
