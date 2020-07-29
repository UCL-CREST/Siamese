    private void replace(ReplacementContext ctx, String regex, String replacement) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ctx.getBuffer().toString());
        while (m.find()) {
            logger.trace(regex);
            logger.trace(ctx.toString());
            int idx = ctx.getBits().nextSetBit(m.start());
            if ((idx == -1) || (idx > m.end() - 1)) {
                int len = m.end() - m.start();
                int offset = replacement.length() - len;
                if (offset > 0) {
                    for (int i = ctx.getBuffer().length() - 1; i > m.end(); i--) {
                        ctx.getBits().set(i + offset, ctx.getBits().get(i));
                    }
                } else if (offset < 0) {
                    for (int i = m.end(); i < ctx.getBuffer().length(); i++) {
                        ctx.getBits().set(i + offset, ctx.getBits().get(i));
                    }
                }
                for (int i = m.start(); i < m.end() + offset; i++) {
                    ctx.getBits().set(i);
                }
                ctx.getBuffer().replace(m.start(), m.end(), replacement);
                logger.trace(ctx.toString());
            }
        }
    }
