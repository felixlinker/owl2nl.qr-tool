public static void main(String[] args) throws Exception {
		LinkedList<TripleHelper> output = new LinkedList<>();
		TripleConverter c = new TripleConverter();

		BufferedReader fileReader = new BufferedReader(new FileReader("C:/Git/owl2nl.qr-tool/data/instances_verb.csv"));

		String line;
		while ((line = fileReader.readLine()) != null) {
			output.add(new TripleHelper(line.replaceAll("\n",""),c));
		}
		fileReader.close();

		FileWriter fw = new FileWriter("C:/Git/owl2nl.qr-tool/data/instances_verb_new.csv");
		for (TripleHelper t: output) {
			fw.append(t.csvLine());
		}
		fw.close();
	}

	public static class TripleHelper {
		public String triple;
		public String verbalization;
		public TripleConverter c;

		public TripleHelper(String triple, TripleConverter c) {
			this.triple = triple;
			this.c = c;
			this.parseTriple();
		}

		private void parseTriple() {
			String[] splitted = triple.split(" ");

			Node third;
			if (splitted[2].contains("^^")) {
				String[] literal = (splitted[2]).split("\\^\\^");
				third = NodeFactory.createLiteral(literal[0].split("\"")[1], XSDDatatype.XSDboolean);
			} else {
				third = NodeFactory.createURI(splitted[2]);
			}

			this.verbalization = c.convert(Triple.create(
					NodeFactory.createURI(splitted[0]),
					NodeFactory.createURI(splitted[1]),
					third
			));
		}

		public String csvLine() {
			StringBuilder builder = new StringBuilder();
			builder.append('\"');
			builder.append(triple);
			builder.append('\"');
			builder.append(';');
			builder.append('\"');
			builder.append(verbalization);
			builder.append('\"');
			builder.append('\n');

			return builder.toString();
		}
	}