    @SuppressWarnings("unchecked")
    private void addClassNamesToCombobox(String type, JComboBox comboBox) {
        List values = comboBoxValues.get(type);
        if (values != null) {
            for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                String value = iterator.next();
                comboBox.addItem(value);
            }
            return;
        } else {
            values = new ArrayList();
            comboBoxValues.put(type, values);
        }
        String packageName = null;
        if (type.equals(Constants.INPUT)) {
            packageName = Constants.INPUT_PACKAGE_NAME;
        } else if (type.equals(Constants.DATA_ENGINE)) {
            packageName = Constants.DATA_ENGINE_PACKAGE_NAME;
        } else if (type.equals(Constants.OUTPUT)) {
            packageName = Constants.OUTPUT_PACKAGE_NAME;
        }
        String folderPath = "/" + packageName;
        folderPath = folderPath.replace('.', '/');
        URL url = Launcher.class.getResource(folderPath);
        File directory = new File(url.getFile());
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(Constants.CLASS_EXTENSION) && !files[i].contains(Constants.INNERCLASS_SYMBOL)) {
                    String classname = files[i].substring(0, files[i].length() - 6);
                    String defaultParms = "";
                    try {
                        Class myclass = Class.forName(packageName + classname);
                        Constructor constructor = myclass.getConstructor();
                        Object myObject = constructor.newInstance();
                        Method getDefaultParmsMethod = myclass.getMethod(Constants.DEFAULT_PARMS_METHOD_NAME);
                        defaultParms = (String) getDefaultParmsMethod.invoke(myObject);
                        Method getHelpTextMethod = myclass.getMethod(Constants.HELP_TEXT_METHOD_NAME);
                        String helpText = (String) getHelpTextMethod.invoke(myObject);
                        helpTextMap.put(classname, helpText);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    comboBox.addItem(classname + " " + defaultParms);
                    values.add(classname + " " + defaultParms);
                }
            }
        }
    }
