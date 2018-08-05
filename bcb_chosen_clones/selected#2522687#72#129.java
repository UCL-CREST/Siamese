    public void update() {
        if (!updatable) {
            Main.fenetre().erreur(Fenetre.OLD_VERSION);
            return;
        }
        try {
            Main.fenetre().update();
            Element remoteRoot = new SAXBuilder().build(xml).getRootElement();
            addPackages = new HashMap<Integer, PackageVersion>();
            Iterator<?> iterElem = remoteRoot.getChildren().iterator();
            while (iterElem.hasNext()) {
                PackageVersion pack = new PackageVersion((Element) iterElem.next());
                addPackages.put(pack.id(), pack);
            }
            removePackages = new HashMap<Integer, PackageVersion>();
            iterElem = root.getChildren("package").iterator();
            while (iterElem.hasNext()) {
                PackageVersion pack = new PackageVersion((Element) iterElem.next());
                int id = pack.id();
                if (!addPackages.containsKey(id)) {
                    removePackages.put(id, pack);
                } else if (addPackages.get(id).version().equals(pack.version())) {
                    addPackages.remove(id);
                } else {
                    addPackages.get(id).ecrase();
                }
            }
            Iterator<PackageVersion> iterPack = addPackages.values().iterator();
            while (iterPack.hasNext()) {
                install(iterPack.next());
            }
            iterPack = removePackages.values().iterator();
            while (iterPack.hasNext()) {
                remove(iterPack.next());
            }
            if (offline) {
                Runtime.getRuntime().addShutdownHook(new AddPackage(xml, "versions.xml"));
                Main.fenetre().erreur(Fenetre.UPDATE_TERMINE_RESTART);
            } else {
                File oldXML = new File("versions.xml");
                oldXML.delete();
                oldXML.createNewFile();
                FileChannel out = new FileOutputStream(oldXML).getChannel();
                FileChannel in = new FileInputStream(xml).getChannel();
                in.transferTo(0, in.size(), out);
                in.close();
                out.close();
                xml.delete();
                if (restart) {
                    Main.fenetre().erreur(Fenetre.UPDATE_TERMINE_RESTART);
                } else {
                    Main.updateVersion();
                }
            }
        } catch (Exception e) {
            Main.fenetre().erreur(Fenetre.ERREUR_UPDATE, e);
        }
    }
