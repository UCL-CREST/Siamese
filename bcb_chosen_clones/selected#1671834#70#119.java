    public void comandoCapitalizar(String comando, int posicionCursor) {
        String capitalizar = primerArgumento(comando);
        String expresionRegular = expresionRegularCorrecta(segundoArgumento(comando));
        String texto = documentoActual.getTexto();
        String textoRemplazado = texto.toString();
        Pattern patron = null;
        Matcher encaja = null;
        try {
            patron = Pattern.compile(expresionRegular);
            encaja = patron.matcher(texto);
        } catch (PatternSyntaxException pse) {
            System.out.println("Expresion regular mal formada");
            return;
        } catch (OutOfMemoryError p) {
            System.out.println("Expresion regular mal formada");
            return;
        }
        Vector reemplazarPalabras = new Vector();
        while (encaja.find(posicionCursor)) {
            reemplazarPalabras.add(texto.substring(encaja.start(), encaja.end()));
            posicionCursor = encaja.end();
        }
        if (reemplazarPalabras.size() > 0) {
            if (capitalizar.equals("M")) {
                for (int i = 0; i < reemplazarPalabras.size(); i++) {
                    String palabraOriginal = (String) reemplazarPalabras.elementAt(i);
                    String palabraModificada = ((String) reemplazarPalabras.elementAt(i)).toUpperCase();
                    imprimirConsola(palabraOriginal, palabraModificada);
                    textoRemplazado = textoRemplazado.replaceAll((String) (reemplazarPalabras.elementAt(i)), ((String) reemplazarPalabras.elementAt(i)).toUpperCase());
                }
            } else if (capitalizar.equals("m")) {
                for (int i = 0; i < reemplazarPalabras.size(); i++) {
                    System.out.println("Cambio: " + (String) reemplazarPalabras.elementAt(i));
                    System.out.println("Por: " + ((String) reemplazarPalabras.elementAt(i)).toLowerCase());
                    textoRemplazado = textoRemplazado.replaceAll((String) reemplazarPalabras.elementAt(i), ((String) reemplazarPalabras.elementAt(i)).toLowerCase());
                }
            } else if (capitalizar.equals("Mm")) {
                for (int i = 0; i < reemplazarPalabras.size(); i++) {
                    String palabra = reemplazarPalabras.elementAt(i).toString();
                    Character letraInicial = new Character(palabra.charAt(0));
                    String letrasFinales = palabra.substring(1, palabra.length());
                    String capitalizada = letraInicial.toUpperCase(palabra.charAt(0)) + letrasFinales.toLowerCase();
                    System.out.println("Cambio: " + palabra);
                    System.out.println("Por: " + capitalizada);
                    textoRemplazado = textoRemplazado.replaceAll((String) reemplazarPalabras.elementAt(i), capitalizada);
                }
            } else this.aplicacion.mensajeDeError("Opcion desconocida");
        } else System.out.println("No hay macheo");
        documentoActual.setTexto(textoRemplazado);
    }
