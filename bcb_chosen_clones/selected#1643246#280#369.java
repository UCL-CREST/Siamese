    public void save(String path) throws CantSaveException, InvalidFileException, CantReplaceFileException {
        CRC32 crc = new CRC32();
        File file = new File(path + File.separator + filename);
        FileUtils.isFileValid(file);
        if (isNew) {
            if (FileUtils.fileExists(path, filename)) {
                throw new CantReplaceFileException(FSMessages.getMessage("flyEditor.alreadyExists"), "filename");
            }
        }
        if (!isNew && !originalFilename.equalsIgnoreCase(filename)) {
            if (FileUtils.fileExists(path, filename)) {
                throw new CantReplaceFileException(FSMessages.getMessage("flyEditor.alreadyExists"), "filename");
            }
        }
        Document doc = new Document();
        doc.addContent(new Comment("This file was created by FlySource. (www.flysource.net)"));
        doc.setRootElement(new Element("FlyPattern"));
        Element root = doc.getRootElement();
        root.setAttribute("version", FlyShareApp.FLY_PATTERN_VERSION);
        Element el = new Element(NAME_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getName()));
        root.addContent(el);
        el = new Element(TYPE_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getType()));
        root.addContent(el);
        el = new Element(ORIGINATOR_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getOriginator()));
        root.addContent(el);
        el = new Element(TIER_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getTier()));
        root.addContent(el);
        el = new Element(REFERENCE_ELEMENT);
        el.setText(StringUtils.trimToEmpty(getReference()));
        root.addContent(el);
        el = new Element(MATERIALS_ELEMENT);
        el.setText(extractMaterials());
        root.addContent(el);
        el = new Element(TYING_INSTRUCTIONS_ELEMENT);
        el.setText(extractTyingInstructions());
        root.addContent(el);
        el = new Element(OTHER_INFO_ELEMENT);
        el.setText(extractOtherInfo());
        root.addContent(el);
        el = new Element(SHAREABLE_ELEMENT);
        el.setText(isShareable() ? "Yes" : "No");
        root.addContent(el);
        crc.update(FlyShareApp.FLY_PATTERN_VERSION.getBytes());
        crc.update(name.getBytes());
        crc.update(type.getBytes());
        if (originator != null) crc.update(originator.getBytes());
        if (detailText != null) crc.update(detailText.getBytes());
        if (reference != null) crc.update(reference.getBytes());
        setHasImage(false);
        if (image != null) {
            try {
                Base64 b64 = new Base64();
                Image i = image.getProcessor().createImage();
                BufferedImage bi = createBufferedImage(i);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bi, "jpg", bos);
                byte[] buffer = bos.toByteArray();
                crc.update(buffer);
                String s = new String(b64.encode(buffer));
                el = new Element(IMAGE_ELEMENT);
                el.setText(s);
                root.addContent(el);
                setHasImage(true);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        el = new Element(CRC_ELEMENT);
        el.setText("" + crc.getValue());
        root.addContent(el);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setTextMode(Format.TextMode.PRESERVE));
            outputter.output(doc, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new CantSaveException("Cannot save.\n\n" + e.getMessage());
        }
        if (!isNew && !originalFilename.equalsIgnoreCase(filename)) {
            File oldFile = new File(path + File.separator + originalFilename);
            oldFile.delete();
        }
        if (!isNew) setChangeSync(true);
        setSentToServer(false);
    }
