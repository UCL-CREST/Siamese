    @Deprecated
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        XUIResponseWriter w = getResponseWriter();
        XUIViewRoot viewRoot = (XUIViewRoot) component;
        XUIResponseWriter headerW = getResponseWriter().getHeaderWriter();
        headerW.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE foo [\n");
        InputStream is1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("xhtml-lat1.ent");
        headerW.write(new String(IOUtils.copyByte(is1)));
        headerW.write("\n");
        is1.close();
        InputStream is2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("xhtml-special.ent");
        headerW.write(new String(IOUtils.copyByte(is2)));
        headerW.write("\n");
        is2.close();
        InputStream is3 = Thread.currentThread().getContextClassLoader().getResourceAsStream("xhtml-symbol.ent");
        headerW.write(new String(IOUtils.copyByte(is3)));
        headerW.write("\n]>\n");
        is3.close();
        headerW.startElement("html", component);
        XUITheme t = getTheme();
        if (t != null) {
            headerW.writeAttribute("style", getTheme().getHtmlStyle(), "style");
        }
        headerW.startElement("head", component);
        headerW.startElement("base", component);
        HttpServletRequest req = (HttpServletRequest) getRequestContext().getRequest();
        String link = (req.isSecure() ? "https" : "http") + "://" + req.getServerName() + (req.getServerPort() == 80 ? "" : ":" + req.getServerPort()) + getRequestContext().getResourceUrl("");
        headerW.writeAttribute("href", link, "href");
        headerW.endElement("base");
        w.startElement("body", component);
        if (t != null && t.getBodyStyle() != null) {
            w.writeAttribute("style", getTheme().getBodyStyle() + ";height:100%;width:100%", "style");
        }
        headerW.writeText('\n');
        w.startElement("div", component);
        w.writeAttribute("id", ((XUIViewRoot) component).getClientId(), "id");
        if (viewRoot.findComponent(Window.class) != null) {
            w.writeAttribute(HTMLAttr.CLASS, "x-panel", "");
        }
        w.writeAttribute("style", "width:100%;height:100%", null);
        if (t != null) {
            t.addScripts(w.getScriptContext());
            t.addStyle(w.getStyleContext());
        }
    }
