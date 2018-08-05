    public void saveToPackage() {
        boolean inPackage = false;
        String className = IconEditor.className;
        if (!checkPackage()) {
            JOptionPane.showMessageDialog(this, "No package selected. Aborting.", "Package not selected!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        File iconFile = new File(getPackageFile().getParent() + File.separator + classIcon);
        File prevIconFile = new File(prevPackagePath + File.separator + classIcon);
        if ((IconEditor.getClassIcon() == null) || !prevIconFile.exists()) {
            IconEditor.setClassIcon("default.gif");
        } else if (prevIconFile.exists() && (prevIconFile.compareTo(iconFile) != 0)) {
            FileFuncs.copyImageFile(prevIconFile, iconFile);
        }
        ci = new ClassImport(getPackageFile(), packageClassNamesList, packageClassList);
        for (int i = 0; i < packageClassList.size(); i++) {
            if (IconEditor.className.equalsIgnoreCase(packageClassList.get(i).getName())) {
                inPackage = true;
                classX = 0 - classX;
                classY = 0 - classY;
                shapeList.shift(classX, classY);
                packageClassList.get(i).setBoundingbox(boundingbox);
                packageClassList.get(i).setDescription(IconEditor.classDescription);
                if (IconEditor.getClassIcon() == null) {
                    packageClassList.get(i).setIconName("default.gif");
                } else {
                    packageClassList.get(i).setIconName(IconEditor.getClassIcon());
                }
                packageClassList.get(i).setIsRelation(IconEditor.classIsRelation);
                packageClassList.get(i).setName(IconEditor.className);
                packageClassList.get(i).setPorts(ports);
                packageClassList.get(i).shiftPorts(classX, classY);
                packageClassList.get(i).setShapeList(shapeList);
                if (dbrClassFields != null && dbrClassFields.getRowCount() > 0) {
                    fields.clear();
                    for (int j = 0; j < dbrClassFields.getRowCount(); j++) {
                        String fieldName = dbrClassFields.getValueAt(j, iNAME);
                        String fieldType = dbrClassFields.getValueAt(j, iTYPE);
                        String fieldValue = dbrClassFields.getValueAt(j, iVALUE);
                        ClassField field = new ClassField(fieldName, fieldType, fieldValue);
                        fields.add(field);
                    }
                }
                packageClassList.get(i).setFields(fields);
                packageClassList.add(packageClassList.get(i));
                packageClassList.remove(i);
                break;
            }
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(getPackageFile()));
            String str;
            StringBuffer content = new StringBuffer();
            while ((str = in.readLine()) != null) {
                if (inPackage && str.trim().startsWith("<class")) {
                    break;
                } else if (!inPackage) {
                    if (str.equalsIgnoreCase("</package>")) break;
                    content.append(str + "\n");
                } else if (inPackage) content.append(str + "\n");
            }
            if (!inPackage) {
                content.append(getShapesInXML(false));
            } else {
                for (int i = 0; i < packageClassList.size(); i++) {
                    classX = 0;
                    classY = 0;
                    makeClass(packageClassList.get(i));
                    content.append(getShapesInXML(false));
                }
            }
            content.append("</package>");
            in.close();
            File javaFile = new File(getPackageFile().getParent() + File.separator + className + ".java");
            File prevJavaFile = new File(prevPackagePath + File.separator + className + ".java");
            int overwriteFile = JOptionPane.YES_OPTION;
            if (javaFile.exists()) {
                overwriteFile = JOptionPane.showConfirmDialog(null, "Java class already exists. Overwrite?");
            }
            if (overwriteFile != JOptionPane.CANCEL_OPTION) {
                FileOutputStream out = new FileOutputStream(new File(getPackageFile().getAbsolutePath()));
                out.write(content.toString().getBytes());
                out.flush();
                out.close();
                if (overwriteFile == JOptionPane.YES_OPTION) {
                    String fileText = null;
                    if (prevJavaFile.exists()) {
                        fileText = FileFuncs.getFileContents(prevJavaFile);
                    } else {
                        fileText = "class " + className + " {";
                        fileText += "\n    /*@ specification " + className + " {\n";
                        for (int i = 0; i < dbrClassFields.getRowCount(); i++) {
                            String fieldName = dbrClassFields.getValueAt(i, iNAME);
                            String fieldType = dbrClassFields.getValueAt(i, iTYPE);
                            if (fieldType != null) {
                                fileText += "    " + fieldType + " " + fieldName + ";\n";
                            }
                        }
                        fileText += "    }@*/\n \n}";
                    }
                    FileFuncs.writeFile(javaFile, fileText);
                }
                JOptionPane.showMessageDialog(null, "Saved to package: " + getPackageFile().getName(), "Saved", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
