    protected void checkForStandardGraphError(JsonNode node) {
        JsonNode errorNode = node.get("error");
        if (errorNode != null) {
            String type = errorNode.path("type").asText();
            if (type == null) return;
            String msg = errorNode.path("message").asText();
            if (msg == null) return;
            if (msg.startsWith("(#200)")) throw new PermissionException(msg);
            if (msg.startsWith("(#21)")) this.throwPageMigratedException(msg);
            String proposedExceptionType = Batcher.class.getPackage().getName() + ".err." + type;
            try {
                Class<?> exceptionClass = Class.forName(proposedExceptionType);
                Constructor<?> ctor = exceptionClass.getConstructor(String.class);
                throw (FacebookException) ctor.newInstance(msg);
            } catch (FacebookException e) {
                throw e;
            } catch (Exception e) {
                throw new FacebookException(type + ": " + msg);
            }
        }
    }
