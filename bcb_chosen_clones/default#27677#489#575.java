    public void compileNameList(ArrayList classNames) {
        String classPath = System.getProperty("java.class.path");
        StringTokenizer st = new StringTokenizer(classPath, File.pathSeparator);
        ArrayList classPathDirs = new ArrayList();
        while (st.hasMoreTokens()) {
            classPathDirs.add(st.nextToken());
        }
        int pathSize = classPathDirs.size();
        int classListSize = classNames.size();
        for (int i = 0; i < classListSize; i++) {
            String className = (String) classNames.get(i);
            String javaFileName = File.separator + className.replace('.', File.separatorChar) + JAVA_ENDING;
            String classFileName = File.separator + className.replace('.', File.separatorChar) + CLASS_ENDING;
            int j = 0;
            boolean found = false;
            String fullJavaFileName = "";
            while ((!found) && (j < pathSize)) {
                String curPath = (String) classPathDirs.get(j);
                fullJavaFileName = curPath.concat(javaFileName);
                String fullClassFileName = curPath.concat(classFileName);
                File javaFile = new File(fullJavaFileName);
                File classFile = new File(fullClassFileName);
                if (javaFile.exists()) {
                    found = true;
                    boolean compile = true;
                    if (classFile.exists()) {
                        if (javaFile.lastModified() <= classFile.lastModified()) compile = false; else classFile.delete();
                    }
                    if ((compile || forceRecompile) && (!compiledClasses.contains(className))) {
                        String command = "javac.exe -classpath " + classPath + " ";
                        for (int k = 0; k < compilerOptions.size(); k++) {
                            String curOption = (String) compilerOptions.get(k);
                            command = (command.concat(curOption)).concat(" ");
                        }
                        command = command.concat(fullJavaFileName);
                        Runtime rt = Runtime.getRuntime();
                        System.out.print("compiling " + className);
                        try {
                            Process p = rt.exec(command);
                            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            boolean startOut = true;
                            String errorLine;
                            String outLine;
                            Matcher m;
                            while ((errorLine = error.readLine()) != null) {
                                if (startOut) {
                                    System.err.println();
                                    startOut = false;
                                }
                                m = errorPattern.matcher(errorLine);
                                if (!m.lookingAt()) System.err.println(errorLine);
                            }
                            startOut = true;
                            while ((outLine = stdOut.readLine()) != null) {
                                if (startOut) {
                                    System.err.println();
                                    startOut = false;
                                }
                                System.out.println(outLine);
                            }
                            int exitValue = p.waitFor();
                            if (exitValue != 0) {
                                errors++;
                                return;
                            } else {
                                filesCompiled++;
                                compiledClasses.add(className);
                                System.out.println(" (done)");
                            }
                        } catch (Exception e) {
                            System.err.println(e.getMessage() + " [command could not be completed]");
                            errors++;
                            filesCompiled--;
                            return;
                        }
                    }
                }
                j++;
            }
            if ((!found) && (!notFoundClasses.contains(className))) {
                System.err.println("could not locate file for " + className);
                notFoundClasses.add(className);
                errors++;
            }
        }
    }
