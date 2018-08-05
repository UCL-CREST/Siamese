    public void comandoReemplazar(String comando, int posicionCursor) {
        String segundoArgumento = segundoArgumento(comando);
        String nombreVariable = nombreVariable(comando);
        String primerArgumento = primerArgumento(comando);
        String expresionRegular = primerArgumento.replace(nombreVariable, "");
        String texto = documentoActual.getTexto();
        System.out.println(comando);
        System.out.println(primerArgumento);
        System.out.println(segundoArgumento);
        System.out.println(nombreVariable);
        System.out.println(expresionRegular);
        Pattern patron = null;
        Matcher matcher = null;
        String expresionRegularCorrecta = expresionRegularCorrecta(expresionRegular);
        System.out.println(expresionRegularCorrecta);
        try {
            patron = Pattern.compile(expresionRegularCorrecta);
            matcher = patron.matcher(texto);
        } catch (PatternSyntaxException pse) {
            System.out.println("Expresion regular mal conformada, por favor consultar sintaxis");
            return;
        }
        while (matcher.find(posicionCursor)) {
            String cadenaOriginal = texto.substring(matcher.start(), matcher.end());
            String[] string = primerArgumento.split("/" + nombreVariable + ".");
            System.out.println(string[0]);
            System.out.println(string[1]);
            String matcheoVariable = cadenaOriginal.replace(string[0], "");
            matcheoVariable = matcheoVariable.replace(string[1], "");
            System.out.println(matcheoVariable);
            String cadenaModificada = segundoArgumento.replace("/" + nombreVariable + "/", matcheoVariable);
            System.out.println(cadenaModificada);
            texto = texto.replaceFirst(cadenaOriginal, cadenaModificada);
            System.out.println(cadenaOriginal + " " + matcher.start() + "-" + matcher.end());
            posicionCursor = matcher.end();
        }
        documentoActual.setTexto(texto);
    }
