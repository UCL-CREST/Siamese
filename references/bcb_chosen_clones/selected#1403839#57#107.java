    public void writeResponse(HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        if (multiResponses == null || !multiResponses.hasNext()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Requested content not found.");
            log.warn("Requested content not found.");
            return;
        }
        response.setContentType(MultiPartContentTypeFilter.APPLICATION_ZIP);
        response.setHeader(CONTENT_DISPOSITION, "attachment;filename=wado.zip");
        ServletOutputStream os = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        int fcnt = 1;
        while (multiResponses.hasNext()) {
            ServletResponseItem sri = multiResponses.next();
            if (sri == null) {
                log.info("Skipping a servlet response with an empty response item.");
                continue;
            }
            if (sri instanceof ErrorResponseItem) {
                log.warn("Skipping an ErrorResponseItem with code: " + ((ErrorResponseItem) sri).getCode());
                continue;
            }
            log.debug("Found a response item to add.");
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BodyResponseWrapper brw = new BodyResponseWrapper(response, baos);
                sri.writeResponse(httpRequest, brw);
                byte[] data = baos.toByteArray();
                Map<String, String> headers = brw.getHeaders();
                String filename = getFilename(headers);
                if (filename == null) filename = "wado-unknown-" + fcnt++;
                log.debug("Writing response named {}", filename);
                ZipEntry ze = new ZipEntry(filename);
                ze.setSize(data.length);
                ze.setMethod(ZipEntry.STORED);
                String comment = headersToString(headers);
                if (comment != null) ze.setComment(comment);
                log.debug("Putting next entry {}", ze);
                CRC32 crc = new CRC32();
                crc.update(data);
                ze.setCrc(crc.getValue());
                zos.putNextEntry(ze);
                log.debug("Write data of size {}", data.length);
                zos.write(data);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("response for content type=\"" + response.getContentType() + "has failed:" + e);
            }
        }
        zos.close();
        os.close();
    }
