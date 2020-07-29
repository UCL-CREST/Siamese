    private static void copierScriptChargement(File webInfDir, String initialDataChoice) {
        File chargementInitialDir = new File(webInfDir, "chargementInitial");
        File fichierChargement = new File(chargementInitialDir, "ScriptChargementInitial.sql");
        File fichierChargementAll = new File(chargementInitialDir, "ScriptChargementInitial-All.sql");
        File fichierChargementTypesDocument = new File(chargementInitialDir, "ScriptChargementInitial-TypesDocument.sql");
        File fichierChargementVide = new File(chargementInitialDir, "ScriptChargementInitial-Vide.sql");
        if (fichierChargement.exists()) {
            fichierChargement.delete();
        }
        File fichierUtilise = null;
        if ("all".equals(initialDataChoice)) {
            fichierUtilise = fichierChargementAll;
        } else if ("typesDocument".equals(initialDataChoice)) {
            fichierUtilise = fichierChargementTypesDocument;
        } else if ("empty".equals(initialDataChoice)) {
            fichierUtilise = fichierChargementVide;
        }
        if (fichierUtilise != null && fichierUtilise.exists()) {
            FileChannel source = null;
            FileChannel destination = null;
            try {
                source = new FileInputStream(fichierUtilise).getChannel();
                destination = new FileOutputStream(fichierChargement).getChannel();
                destination.transferFrom(source, 0, source.size());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (source != null) {
                    try {
                        source.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (destination != null) {
                    try {
                        destination.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
