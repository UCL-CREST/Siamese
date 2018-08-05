    public ArrayList<Tupla> desmembrar(String miCadena) {
        ArrayList<Tupla> miArrayList = new ArrayList<Tupla>();
        String palabraEncajada = new String();
        Boolean valorBooleano;
        Pattern patron = Pattern.compile(palabrasSeparadorasRegex);
        Matcher encaja = patron.matcher(miCadena);
        while (encaja.find()) {
            palabraEncajada = miCadena.substring(encaja.start(), encaja.end());
            if (palabrasSeparadoras.contains(String.valueOf(palabraEncajada.charAt(0)))) {
                valorBooleano = true;
            } else {
                valorBooleano = false;
            }
            miArrayList.add(new Tupla(palabraEncajada, valorBooleano));
        }
        return miArrayList;
    }
