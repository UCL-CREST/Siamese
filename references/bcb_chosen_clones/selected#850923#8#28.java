    public static void main(String[] args) {
        try {
            URL url = new URL("http://www.lineadecodigo.com");
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (Throwable t) {
            }
            String inputLine;
            String inputText = "";
            while ((inputLine = in.readLine()) != null) {
                inputText = inputText + inputLine;
            }
            System.out.println("El contenido de la URL es: " + inputText);
            in.close();
        } catch (MalformedURLException me) {
            System.out.println("URL erronea");
        } catch (IOException ioe) {
            System.out.println("Error IO");
        }
    }
