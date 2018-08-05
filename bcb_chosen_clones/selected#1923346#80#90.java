    @SuppressWarnings("unchecked")
    public String toString() {
        try {
            Class<? extends TimeEstimateFormat> formatClass = (Class<? extends TimeEstimateFormat>) Class.forName(localized_eta_format);
            Constructor<? extends TimeEstimateFormat> constructor = formatClass.getConstructor();
            TimeEstimateFormat format = constructor.newInstance();
            return "ETA: " + format.format(this);
        } catch (Exception e) {
            return "ETA: " + super.toString();
        }
    }
