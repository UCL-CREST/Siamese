    public InsertRemoveHandler(Form form, JTree jTree, Element fieldDescriptor) {
        this.form = form;
        this.jTree = jTree;
        this.descriptor = (Element) fieldDescriptor.getElementsByTagName("insertRemoveRestrictions").item(0);
        this.removeRestrictions = this.descriptor.getElementsByTagName("removable");
        this.insertRestrictions = this.descriptor.getElementsByTagName("insertable");
        this.copyRestrictions = this.descriptor.getElementsByTagName("copyable");
        this.pasteRestrictions = this.descriptor.getElementsByTagName("pasteable");
        try {
            this.nodeNameGenerator = (NodeNameGenerator) Class.forName(NAME_GENERATOR).getConstructor(new Class[] { Element.class }).newInstance(new Object[] { fieldDescriptor });
        } catch (Exception e) {
            LOG.error(e);
        }
        Remove.initRemoveConfirmable(this, (Element) (removeRestrictions.getLength() > 0 ? removeRestrictions.item(0) : descriptor));
        jTree.addKeyListener(this);
        jTree.addMouseListener(this);
    }
