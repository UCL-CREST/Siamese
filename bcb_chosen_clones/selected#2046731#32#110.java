    public void init() {
        String inputLine = "";
        String registeredLine = "";
        println("Insert RSS link:");
        String urlString = sc.nextLine();
        if (urlString.length() == 0) init();
        println("Working...");
        BufferedReader in = null;
        URL url = null;
        try {
            url = new URL(urlString);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((inputLine = in.readLine()) != null) registeredLine += inputLine;
            in.close();
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File elenco = new File("elenco.txt");
        PrintWriter pout = null;
        try {
            pout = new PrintWriter(elenco);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        Vector<String> vector = new Vector<String>();
        int endIndex = 0;
        int numeroFoto = 0;
        while ((registeredLine = registeredLine.substring(endIndex)).length() > 10) {
            int startIndex = registeredLine.indexOf("<media:content url='");
            if (startIndex == -1) break;
            registeredLine = registeredLine.substring(startIndex);
            String address = "";
            startIndex = registeredLine.indexOf("http://");
            endIndex = registeredLine.indexOf("' height");
            address = registeredLine.substring(startIndex, endIndex);
            println(address);
            pout.println(address);
            vector.add(address);
            numeroFoto++;
        }
        if (pout.checkError()) println("ERROR");
        println("Images number: " + numeroFoto);
        if (numeroFoto == 0) {
            println("No photos found, WebAlbum is empty or the RSS link is incorrect.");
            sc.nextLine();
            System.exit(0);
        }
        println("Start downloading? (y/n)");
        if (!sc.nextLine().equalsIgnoreCase("y")) System.exit(0);
        SimpleDateFormat data = new SimpleDateFormat("dd-MM-yy_HH.mm");
        Calendar oggi = Calendar.getInstance();
        String cartella = data.format(oggi.getTime());
        boolean success = new File(cartella).mkdir();
        if (success) println("Sub-directory created...");
        println("downloading...\npress ctrl-C to stop");
        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        URL photoAddr = null;
        int len = 0;
        for (int x = 0; x < vector.size(); x++) {
            println("file " + (x + 1) + " of " + numeroFoto);
            try {
                photoAddr = new URL(vector.get(x));
                bin = new BufferedInputStream(photoAddr.openStream());
                bout = new BufferedOutputStream(new FileOutputStream(cartella + "/" + (x + 1) + ".jpg"));
                while ((len = bin.read()) != -1) bout.write(len);
                bout.flush();
                bout.close();
                bin.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        println("Done!");
    }
