    protected ModelAndView handleRequestAfterValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filename = getFilename();
        response.setContentType(mimeTypes.getContentType(filename));
        response.setHeader("Cache-Control", "private");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        String encoding = "UTF-8";
        zipOut.setEncoding(encoding);
        Map formData = request.getParameterMap();
        Iterator itFormData = formData.entrySet().iterator();
        while (itFormData.hasNext()) {
            Map.Entry me = (Map.Entry) itFormData.next();
            if (((String) me.getKey()).startsWith("id_")) {
                String defId = ((String) me.getKey()).substring(3);
                if (defId.startsWith("%")) defId = defId.substring(1);
                if (Validator.isNotNull(defId)) {
                    try {
                        NamedDocument doc = getDocumentForId(defId);
                        zipOut.putNextEntry(new ZipEntry(Validator.replacePathCharacters(doc.name) + ".xml"));
                        XmlFileUtil.writeFile(doc.doc, zipOut);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        zipOut.finish();
        return null;
    }
