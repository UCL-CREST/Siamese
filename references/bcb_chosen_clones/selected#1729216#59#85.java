    public JSONObject getTargetGraph(HttpSession session, JSONObject json) throws JSONException {
        StringBuffer out = new StringBuffer();
        Graph tgt = null;
        MappingManager manager = (MappingManager) session.getAttribute(RuncibleConstants.MAPPING_MANAGER.key());
        try {
            tgt = manager.getTargetGraph();
            if (tgt != null) {
                FlexGraphViewFactory factory = new FlexGraphViewFactory();
                factory.setColorScheme(ColorSchemes.ORANGES);
                factory.visit(tgt);
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
                out.append("No target graph loaded.");
            }
        } catch (Exception e) {
            return JSONUtils.SimpleJSONError("Cannot load target graph: " + e.getMessage());
        }
        return JSONUtils.SimpleJSONResponse(out.toString());
    }
