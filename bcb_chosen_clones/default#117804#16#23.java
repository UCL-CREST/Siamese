    public static void main(String[] args) throws Exception {
        String xml = "<?xml version='1.0' encoding='ISO-8859-1'?><GrowthChart><myaction>clearLenageinf</myaction>" + "<Sex>1</Sex><Agemonth>0.0</Agemonth><L>1.2670042261</L><M>49.988884079</M><S>0.0531121908</S>" + "<P3>44.92509779760535</P3><P5>45.568409099795254</P5><P10>46.554293019128345</P10><P25>48.18937381404629</P25>" + "<P50>49.988884079</P50><P75>51.77125748526315</P75><P90>53.36153474895388</P90><P95>54.3072119715771</P95>" + "<P97>54.91900445357619</P97><Pub3>45.006125533</Pub3><Pub5>45.639150747</Pub5><Pub10>46.609256641</Pub10>" + "<Pub25>48.218174004</Pub25><Pub50>49.988884079</Pub50><Pub75>51.742731577</Pub75><Pub90>53.307562321</Pub90>" + "<Pub95>54.238104965</Pub95><Pub97>54.840115281</Pub97><Diff3>-0.08102773539464891</Diff3><Diff5>-0.0707416472047484</Diff5>" + "<Diff10>-0.05496362187165715</Diff10><Diff25>-0.02880018995370648</Diff25><Diff50>0.0</Diff50>" + "<Diff75>0.02852590826314838</Diff75><Diff90>0.05397242795388024</Diff90><Diff95>0.06910700657709867</Diff95>" + "<Diff97>0.07888917257618999</Diff97></GrowthChart>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        System.out.println((String) path("/GrowthChart/action/text()").evaluate(doc, XPathConstants.STRING));
    }
