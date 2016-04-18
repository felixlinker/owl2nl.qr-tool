public static void main(String[] args) {
        SparqlEndpoint endpoint = SparqlEndpoint.getEndpointDBpedia();
        Verbalizer verbalizer = new Verbalizer(endpoint, "C:/Git/owl2nl.qr-tool/data/verbalizer.txt", "C:/Program Files (x86)/WordNet/2.1");
        TripleConverter converter = new TripleConverter(endpoint);

        PrefixMapping mapping = PrefixMapping.Factory.create();
        mapping.setNsPrefix("dbr", "http://dbpedia.org/resource/");
        mapping.setNsPrefix("dbo", "http://dbpedia.org/ontology/");
        mapping.setNsPrefix("xs", "http://www.w3.org/2001/XMLSchema#");
        mapping.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");


        String[] resources = new String[] { "http://dbpedia.org/resource/Albert_Einstein", "http://dbpedia.org/resource/Michael_Jackson", "http://dbpedia.org/resource/Barack_Obama" , "http://dbpedia.org/resource/Angela_Merkel", "http://dbpedia.org/resource/Microsoft", "http://dbpedia.org/resource/Facebook" };
        LinkedList<Summary> summaries = new LinkedList<>();

        for (String resource: resources) {
            OWLIndividual ind = new OWLNamedIndividualImpl(IRI.create(resource));
            String summary = verbalizer.summarize(ind);
            Collection<Triple> triples = verbalizer.getSummaryTriples(ind);
            LinkedList<TripleHelper> cluster = new LinkedList<>();
            for (Triple t: triples) {
                String verbalization = converter.convertTripleToText(t);
                cluster.add(new TripleHelper(mapping, t, verbalization));
            }

            summaries.add(new Summary(resource, summary, cluster));
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter("C:/Git/owl2nl.qr-tool/data/resources.csv");
            for (Summary summary: summaries) {
                fw.append(summary.printCsvLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File writing error");
            return;
        } finally {
            try {
                fw.close();
            } catch (IOException | NullPointerException e) {}
        }
    }

    private static class Summary {
        private String resource;
        private String summary;
        private Collection<TripleHelper> cluster;
        public Summary(String resource, String summary, Collection<TripleHelper> cluster) {
            this.resource = resource;
            this.summary = summary;
            this.cluster = cluster;
        }

        public String printCsvLine() {
            StringBuilder output = new StringBuilder();
            output.append(resource);
            output.append(';');
            output.append(summary);
            output.append(';');
            for (TripleHelper triple: cluster) {
                output.append(triple.toCsv());
            }
            output.append('\n');

            return output.toString();
        }
    }

    private static class TripleHelper {
        private PrefixMapping mapping;
        private Triple t;
        private String verb;
        public TripleHelper(PrefixMapping mapping, Triple t, String verb) {
            this.mapping = mapping;
            this.t = t;
            this.verb = verb;
        }

        public String toCsv() {
            StringBuilder builder = new StringBuilder();
            builder.append(t.toString(mapping));
            builder.append(';');
            builder.append(verb);
            builder.append(';');
            return builder.toString();
        }
    }