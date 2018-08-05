    private void search(Class clazz, Collection result) {
        if (DEBUG) {
            System.err.println("Searching for " + clazz.getName() + " in " + clazz.getClassLoader() + " from " + this);
        }
        String res = "META-INF/services/" + clazz.getName();
        Enumeration en;
        try {
            en = loader.getResources(res);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        List foundClasses = new ArrayList();
        Collection removeClasses = new ArrayList();
        boolean foundOne = false;
        while (en.hasMoreElements()) {
            if (!foundOne) {
                foundOne = true;
                Class realMcCoy = null;
                try {
                    realMcCoy = loader.loadClass(clazz.getName());
                } catch (ClassNotFoundException cnfe) {
                }
                if (realMcCoy != clazz) {
                    if (DEBUG) {
                        if (realMcCoy != null) {
                            System.err.println(clazz.getName() + " is not the real McCoy! Actually found it in " + realMcCoy.getClassLoader());
                        } else {
                            System.err.println(clazz.getName() + " could not be found in " + loader);
                        }
                    }
                    return;
                }
            }
            URL url = (URL) en.nextElement();
            Item currentItem = null;
            try {
                InputStream is = url.openStream();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        line = line.trim();
                        if (line.startsWith("#position=")) {
                            if (currentItem == null) {
                                assert false : "Found line '" + line + "' but there is no item to associate it with!";
                            }
                            try {
                                currentItem.position = Integer.parseInt(line.substring(10));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        if (currentItem != null) {
                            insertItem(currentItem, foundClasses);
                            currentItem = null;
                        }
                        if (line.length() == 0) {
                            continue;
                        }
                        boolean remove = false;
                        if (line.charAt(0) == '#') {
                            if ((line.length() == 1) || (line.charAt(1) != '-')) {
                                continue;
                            }
                            remove = true;
                            line = line.substring(2);
                        }
                        Class inst = null;
                        try {
                            inst = Class.forName(line, false, loader);
                        } catch (ClassNotFoundException cnfe) {
                            if (remove) {
                                continue;
                            } else {
                                throw cnfe;
                            }
                        }
                        if (!clazz.isAssignableFrom(inst)) {
                            if (DEBUG) {
                                System.err.println("Not a subclass");
                            }
                            throw new ClassNotFoundException(inst.getName() + " not a subclass of " + clazz.getName());
                        }
                        if (remove) {
                            removeClasses.add(inst);
                        } else {
                            currentItem = new Item();
                            currentItem.clazz = inst;
                        }
                    }
                    if (currentItem != null) {
                        insertItem(currentItem, foundClasses);
                        currentItem = null;
                    }
                } finally {
                    is.close();
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (DEBUG) {
            System.err.println("Found impls of " + clazz.getName() + ": " + foundClasses + " and removed: " + removeClasses + " from: " + this);
        }
        foundClasses.removeAll(removeClasses);
        Iterator it = foundClasses.iterator();
        while (it.hasNext()) {
            Item item = (Item) it.next();
            if (removeClasses.contains(item.clazz)) {
                continue;
            }
            result.add(new P(item.clazz));
        }
    }
