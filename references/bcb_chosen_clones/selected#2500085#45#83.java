    CompiledTokenizedLM(ObjectInput in) throws IOException, ClassNotFoundException {
        String tokenizerClassName = in.readUTF();
        if (tokenizerClassName.equals("")) {
            mTokenizerFactory = (TokenizerFactory) in.readObject();
        } else {
            try {
                Class<?> tokenizerClass = Class.forName(tokenizerClassName);
                @SuppressWarnings({ "unchecked", "rawtypes" }) Constructor<? extends TokenizerFactory> tokCons = (Constructor<? extends TokenizerFactory>) tokenizerClass.getConstructor(new Class[0]);
                mTokenizerFactory = (TokenizerFactory) tokCons.newInstance(new Object[0]);
            } catch (NoSuchMethodException e) {
                throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
            } catch (InstantiationException e) {
                throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
            } catch (IllegalAccessException e) {
                throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
            } catch (InvocationTargetException e) {
                throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
            }
        }
        mSymbolTable = (SymbolTable) in.readObject();
        mUnknownTokenModel = (LanguageModel.Sequence) in.readObject();
        mWhitespaceModel = (LanguageModel.Sequence) in.readObject();
        mMaxNGram = in.readInt();
        int numNodes = in.readInt();
        int lastInternalNodeIndex = in.readInt();
        mTokens = new int[numNodes];
        mLogProbs = new float[numNodes];
        mLogLambdas = new float[lastInternalNodeIndex + 1];
        mFirstChild = new int[lastInternalNodeIndex + 2];
        mFirstChild[mFirstChild.length - 1] = numNodes;
        for (int i = 0; i < numNodes; ++i) {
            mTokens[i] = in.readInt();
            mLogProbs[i] = in.readFloat();
            if (i <= lastInternalNodeIndex) {
                mLogLambdas[i] = in.readFloat();
                mFirstChild[i] = in.readInt();
            }
        }
    }
