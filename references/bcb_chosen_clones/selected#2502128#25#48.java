    public static void init() {
        armyKits = new Vector();
        try {
            Class armyKitClass = Class.forName("gothwag.armies.ArmyKit");
            ClassLoader cl = armyKitClass.getClassLoader();
            URL u = cl.getResource("gothwag/armies");
            System.out.println(u);
            File theFile = new File(new URI(u.toString()));
            File[] children = theFile.listFiles();
            for (int k = 0; k < children.length; k++) {
                String name = children[k].getName();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.length() - 6);
                    Class newClass = Class.forName("gothwag.armies." + name);
                    if (newClass.getSuperclass().equals(armyKitClass)) {
                        armyKits.add(newClass.getConstructor(new Class[0]).newInstance(new Object[0]));
                        System.out.println("Adding ArmyKit: " + name);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
