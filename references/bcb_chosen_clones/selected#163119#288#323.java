    private void loadTeam(File jarFile, DefendingSide side) {
        if (!jarFile.getName().endsWith(".jar")) {
        }
        try {
            URL[] urlList = { jarFile.toURL() };
            System.out.println("classloader try");
            ClassLoader loader = new URLClassLoader(urlList);
            JarFile jf = new JarFile(jarFile);
            Team team = null;
            for (Enumeration e = jf.entries(); e.hasMoreElements(); ) {
                String jarEntry = e.nextElement().toString();
                if (jarEntry.endsWith(".class")) {
                    jarEntry = jarEntry.replace('/', '.');
                    jarEntry = jarEntry.substring(0, jarEntry.length() - 6);
                    Class c = loader.loadClass(jarEntry);
                    if (c.getSuperclass().equals(Team.class)) {
                        System.out.println("Found a team! (" + c.getCanonicalName() + ")");
                        Class[] types = new Class[] { DefendingSide.class };
                        Constructor cons = c.getConstructor(types);
                        Object[] args = new Object[] { side };
                        team = (Team) cons.newInstance(args);
                    }
                }
            }
            if (side == DefendingSide.East) {
                eastTeam = team;
            } else {
                westTeam = team;
            }
            if (eastTeam != null && westTeam != null) {
                initializeThread();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
