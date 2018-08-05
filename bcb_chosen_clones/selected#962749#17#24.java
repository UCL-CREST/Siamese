    public void show(HttpServletRequest request, HttpServletResponse response, String pantalla, Atributos modelos) {
        URL url = getRecurso(pantalla);
        try {
            IOUtils.copy(url.openStream(), response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
