    public static void verifierSiDerniereVersionDesPluginsMenus(ControleurDeMenu i) {
        if (i.getURLFichierInfoDerniereVersion() == null || i.getURLFichierInfoDerniereVersion() == "") {
            System.err.println("Evenements.java:verifierSiDerniereVersionDesPluginsMenus impossible:\n" + "pour le plugin chargeur de menu :" + i.getNomPlugin());
        }
        if (i.getVersionPlugin() == 0) {
            System.err.println("version non renseignee pour :" + i.getNomPlugin() + " on continue sur le plugin suivant");
            return;
        }
        URL url;
        try {
            url = new URL(i.getURLFichierInfoDerniereVersion());
        } catch (MalformedURLException e1) {
            System.err.println("impossible d'ouvrir l'URL (url mal formee)" + i.getURLFichierInfoDerniereVersion() + "\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            System.err.println("impossible d'ouvrir l'URL (destination inaccessible)" + i.getURLFichierInfoDerniereVersion() + "\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        File destination;
        try {
            destination = File.createTempFile("SimplexeReseau" + compteurDeFichiersTemporaires, ".buf");
        } catch (IOException e1) {
            System.err.println("impossible de creer le fichier temporaire\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        compteurDeFichiersTemporaires++;
        destination.deleteOnExit();
        java.io.InputStream sourceFile = null;
        java.io.FileOutputStream destinationFile = null;
        try {
            destination.createNewFile();
        } catch (IOException e) {
            System.err.println("impossible de creer un fichier temporaire\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        sourceFile = is;
        try {
            destinationFile = new FileOutputStream(destination);
        } catch (FileNotFoundException e) {
            System.err.println("impossible d'ouvrir le flux reseau\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        byte buffer[] = new byte[512 * 1024];
        int nbLecture;
        try {
            while ((nbLecture = sourceFile.read(buffer)) != -1) {
                destinationFile.write(buffer, 0, nbLecture);
            }
        } catch (IOException e) {
            System.err.println("impossible d'ecrire dans le fichier temporaire\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        try {
            sourceFile.close();
            destinationFile.close();
        } catch (IOException e) {
            System.err.println("impossible de fermer le fichier temporaire ou le flux reseau\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        BufferedReader lecteurAvecBuffer = null;
        String ligne;
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(destination));
        } catch (FileNotFoundException e) {
            System.err.println("impossible d'ouvrir le fichier temporaire apres sa creation (contacter un developpeur)\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        try {
            boolean estLaDerniereVersion = true;
            String URLRecupererDerniereVersion = null;
            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                if (ligne.startsWith("version:")) {
                    if (ligne.equals("version:" + i.getVersionPlugin())) {
                    } else {
                        System.err.println("la version pour " + i.getNomPlugin() + " est depassee (" + i.getVersionPlugin() + " alors que la " + ligne + "est disponible)");
                        estLaDerniereVersion = false;
                    }
                }
                if (ligne.startsWith("url:")) {
                    URLRecupererDerniereVersion = ligne.substring(4, ligne.length());
                }
            }
            if (!estLaDerniereVersion && URLRecupererDerniereVersion != null) {
                TelechargerPluginEtCharger(i, URLRecupererDerniereVersion);
            } else {
                System.out.println("on est a la derniere version du plugin " + i.getNomPlugin());
            }
        } catch (IOException e) {
            System.err.println("impossible de lire le fichier temporaire apres sa creation\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        try {
            lecteurAvecBuffer.close();
        } catch (IOException e) {
            return;
        }
    }
