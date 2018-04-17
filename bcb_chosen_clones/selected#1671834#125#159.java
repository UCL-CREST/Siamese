    public void comandoBuscar(String comando) {
        seleccionadas.clear();
        if (cursorEnFinDeTexto()) {
            this.aplicacion.limpiar();
            this.aplicacion.msjResultado("El cursor no debería estar en el final del texto.");
            System.out.println("El cursor no debería estar en el final del texto.");
        } else {
            String expresionRegular = expresionRegularCorrecta(primerArgumento(comando));
            String texto = documentoActual.getTexto();
            Pattern patron = null;
            Matcher encaja = null;
            try {
                patron = Pattern.compile(expresionRegular);
                encaja = patron.matcher(texto);
            } catch (PatternSyntaxException pse) {
                System.out.print("Expresion regular mal conformada, por favor consultar sintaxis");
                return;
            }
            int posicionCursor = documentoActual.getCursorPosition();
            this.aplicacion.limpiar();
            this.aplicacion.msjResultado("Resultado de " + comando);
            System.out.println("Resultado de buscar con " + comando);
            System.out.println("Expresion Regular: " + expresionRegular);
            while (encaja.find(posicionCursor)) {
                System.out.println(texto.substring(encaja.start(), encaja.end()) + " " + encaja.start() + "-" + encaja.end());
                int[] posiciones = { encaja.start(), encaja.end() };
                seleccionadas.addElement(posiciones);
                posicionCursor = encaja.end();
            }
            int[] posiciones = (int[]) seleccionadas.elementAt(seleccion);
            documentoActual.setSelect(posiciones[0], posiciones[1]);
            System.out.println(posiciones[0] + " " + posiciones[1] + " " + seleccion);
            seleccion++;
        }
    }
