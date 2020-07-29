    public boolean saveVideoXMLOnWebserver() {
        String text = "";
        boolean erg = false;
        try {
            URL url = new URL("http://localhost:8080/virtPresenterVerwalter/videofile.jsp?id=" + this.getId());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String zeile = "";
            while ((zeile = in.readLine()) != null) {
                text += zeile + "\n";
            }
            in.close();
            http.disconnect();
            erg = saveVideoXMLOnWebserver(text);
            System.err.println("Job " + this.getId() + " erfolgreich bearbeitet!");
        } catch (MalformedURLException e) {
            System.err.println("Job " + this.getId() + ": Konnte video.xml nicht erstellen. Verbindung konnte nicht aufgebaut werden.");
            return false;
        } catch (IOException e) {
            System.err.println("Job " + this.getId() + ": Konnte video.xml nicht erstellen. Konnte Daten nicht lesen/schreiben.");
            return false;
        }
        return erg;
    }
