    private void unquote(ReplacementContext ctx) {
        Pattern p = Pattern.compile("'[^']+'");
        Matcher m = p.matcher(ctx.getBuffer().toString());
        while (m.find()) {
            logger.trace(ctx.toString());
            int offset = -2;
            for (int i = m.end(); i < ctx.getBuffer().length(); i++) {
                ctx.getBits().set(i + offset, ctx.getBits().get(i));
            }
            for (int i = m.start(); i < m.end() + offset; i++) {
                ctx.getBits().set(i);
            }
            ctx.getBuffer().replace(m.start(), m.start() + 1, "");
            ctx.getBuffer().replace(m.end() - 2, m.end() - 1, "");
            logger.trace(ctx.toString());
        }
        p = Pattern.compile("''");
        m = p.matcher(ctx.getBuffer().toString());
        while (m.find()) {
            logger.trace(ctx.toString());
            int offset = -1;
            for (int i = m.end(); i < ctx.getBuffer().length(); i++) {
                ctx.getBits().set(i + offset, ctx.getBits().get(i));
            }
            for (int i = m.start(); i < m.end() + offset; i++) {
                ctx.getBits().set(i);
            }
            ctx.getBuffer().replace(m.start(), m.start() + 1, "");
            logger.trace(ctx.toString());
        }
    }
