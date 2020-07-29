    public synchronized Format getFormat() {
        if (format == null && formatClass != null) {
            try {
                if (formatFormat != null) {
                    Class<?>[] types = { String.class };
                    format = (Format) Class.forName(formatClass).getConstructor(types).newInstance(formatFormat);
                } else format = (Format) Class.forName(formatClass).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (format == null) {
            if (dataType.equals(DT_ENUM)) format = new EnumTextFormat(); else format = new TrivialTextFormat();
        }
        return format;
    }
