    public Render get(Object object, String mode, NodeLink nodeLink) {
        Class<?> renderClass;
        Render render = null;
        String code = "";
        if (object instanceof Node) {
            Node node = (Node) object;
            if (node.isCatalog()) code = "catalog"; else if (node.isCollection()) code = "collection"; else if (node.isContainer()) code = "container"; else if (node.isDesktop()) code = "desktop"; else if (node.isDocument()) code = "document"; else if (node.isForm()) code = "form";
        } else if (object instanceof Field) code = "field"; else if (object instanceof Task) code = "task"; else if (object instanceof TaskList) code = "tasklist"; else if (object instanceof NotificationList) code = "notificationlist";
        if (code.isEmpty()) return null;
        code += (mode != null) ? mode : "";
        try {
            renderClass = (Class<?>) this.renders.get(code);
            Constructor<?> constructor = renderClass.getConstructor(NodeLink.class);
            render = (Render) constructor.newInstance(nodeLink);
        } catch (NullPointerException oException) {
            throw new SystemException(ErrorCode.RENDERS_FACTORY, code, oException);
        } catch (Exception oException) {
            throw new SystemException(ErrorCode.RENDERS_FACTORY, code, oException);
        }
        return render;
    }
