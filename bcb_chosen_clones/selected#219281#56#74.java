    private BinaryDocument documentFor(String code, String type, int diagramIndex) {
        code = code.replaceAll("\n", "").replaceAll("\t", "").trim().replaceAll(" ", "%20");
        StringBuilder builder = new StringBuilder("http://yuml.me/diagram/");
        builder.append(type).append("/");
        builder.append(code);
        URL url;
        try {
            url = new URL(builder.toString());
            String name = "uml" + diagramIndex + ".png";
            diagramIndex++;
            BinaryDocument pic = new BinaryDocument(name, "image/png");
            IOUtils.copy(url.openStream(), pic.getContent().getOutputStream());
            return pic;
        } catch (MalformedURLException e) {
            throw ManagedIOException.manage(e);
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }
