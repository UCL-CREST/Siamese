    public C classify(MapEntity entity) {
        C result = null;
        for (RuleGroup<C> rg : rules) {
            String attValue = entity.getAttributeValue(rg.attName);
            if (attValue != null) {
                int min = 0;
                int max = rg.attValueRules.size() - 1;
                int curr;
                int cr;
                Rule<C> currRule;
                while (min <= max) {
                    curr = (min + max) / 2;
                    currRule = rg.attValueRules.get(curr);
                    cr = attValue.compareTo(currRule.attValue);
                    if (cr < 0) max = curr - 1; else if (cr > 0) min = curr + 1; else {
                        result = currRule.subClassifier.classify(entity);
                        break;
                    }
                }
                if (result == null && rg.defaultSubClassifier != null) {
                    result = rg.defaultSubClassifier.classify(entity);
                }
                if (result != null) return result;
            }
        }
        return defaultEntityClass;
    }
