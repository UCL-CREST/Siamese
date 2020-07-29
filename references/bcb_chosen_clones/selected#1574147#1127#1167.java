        private File magiaImagen(String titulo) throws MalformedURLException, IOException {
            titulo = URLEncoder.encode("\"" + titulo + "\"", "UTF-8");
            setMessage("Buscando portada en google...");
            URL url = new URL("http://images.google.com/images?q=" + titulo + "&imgsz=small|medium|large|xlarge");
            setMessage("Buscando portada en google: conectando...");
            URLConnection urlCon = url.openConnection();
            urlCon.setRequestProperty("User-Agent", "MyBNavigator");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), Charset.forName("ISO-8859-1")));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            inputLine = sb.toString();
            String busqueda = "<a href=/imgres?imgurl=";
            setMessage("Buscando portada en google: analizando...");
            while (inputLine.indexOf(busqueda) != -1) {
                int posBusqueda = inputLine.indexOf(busqueda) + busqueda.length();
                int posFinal = inputLine.indexOf("&", posBusqueda);
                String urlImagen = inputLine.substring(posBusqueda, posFinal);
                switch(confirmarImagen(urlImagen)) {
                    case JOptionPane.YES_OPTION:
                        setMessage("Descargando imagen...");
                        URL urlImg = new URL(urlImagen);
                        String ext = urlImagen.substring(urlImagen.lastIndexOf(".") + 1);
                        File f = File.createTempFile("Ignotus", "." + ext);
                        BufferedImage image = ImageIO.read(urlImg);
                        FileOutputStream outer = new FileOutputStream(f);
                        ImageIO.write(image, ext, outer);
                        outer.close();
                        in.close();
                        return f;
                    case JOptionPane.CANCEL_OPTION:
                        in.close();
                        return null;
                    default:
                        inputLine = inputLine.substring(posBusqueda + busqueda.length());
                }
            }
            return null;
        }
