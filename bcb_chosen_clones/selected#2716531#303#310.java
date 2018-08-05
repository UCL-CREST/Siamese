        @SuppressWarnings("unchecked")
        public void loadFromXml(Element node, XmlFile xmlDoc) throws Exception {
            String className = node.getAttribute("class");
            Class<? extends ms.jasim.framework.IEvaluator> evalClass = (Class<? extends ms.jasim.framework.IEvaluator>) Class.forName(className);
            Constructor<? extends ms.jasim.framework.IEvaluator> ctor = evalClass.getConstructor();
            instance = ctor.newInstance();
            extractEvaluatorInfo(evalClass);
        }
