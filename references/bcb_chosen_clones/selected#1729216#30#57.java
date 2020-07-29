    public JSONObject getSourceGraph(HttpSession session, JSONObject json) throws JSONException {
        StringBuffer out = new StringBuffer();
        Graph src = null;
        MappingManager manager = (MappingManager) session.getAttribute(RuncibleConstants.MAPPING_MANAGER.key());
        try {
            src = manager.getSourceGraph();
            if (src != null) {
                FlexGraphViewFactory factory = new FlexGraphViewFactory();
                factory.setColorScheme(ColorSchemes.BLUES);
                factory.visit(src);
                GraphView view = factory.getGraphView();
                GraphViewRenderer renderer = new FlexGraphViewRenderer();
                renderer.setGraphView(view);
                InputStream xmlStream = renderer.renderGraphView();
                StringWriter writer = new StringWriter();
                IOUtils.copy(xmlStream, writer);
                writer.close();
                System.out.println(writer.toString());
                out.append(writer.toString());
            } else {
                out.append("No source graph loaded.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSONUtils.SimpleJSONError("Cannot load source graph: " + e.getMessage());
        }
        return JSONUtils.SimpleJSONResponse(out.toString());
    }
