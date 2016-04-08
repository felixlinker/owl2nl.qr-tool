package org.aksw.simba.owl2nl.qr.data;

/**
 * Helper class to do simple string formatting
 */
public class OWL2NL_QRSimpleFormatter {
    /**
     * Formats a string by mapping every ? in it with an argument
     * @param query String containing ?s to map
     * @param args Arguments that will mapped successive to each ?
     * @return composed string
     * @throws IllegalArgumentException Thrown if the amount of args does not fit the amount of ?s
     */
    public static String compose(String query, Object ...args) throws IllegalArgumentException {
        String[] splittedQuery = query.split("\\?");
        boolean lastIsQuestionMark = query.charAt(query.length() - 1) == '?';

        if (splittedQuery.length + (lastIsQuestionMark ? 1 : 0) != args.length + 1) {
            throw new IllegalArgumentException("For every ? there must be exactly one argument");
        }

        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (;i < args.length; i++) {
            builder.append(splittedQuery[i]);
            builder.append(args[i]);
        }

        if (!lastIsQuestionMark) {
            builder.append(splittedQuery[i]);
        }

        return builder.toString();
    }

    /* Only for debugging
    public static void main(String[] args) {
        for (String splitted: "?,?,?".split("\\?")) {
            System.out.print(".");
            System.out.print(splitted);
            System.out.println(".");
        }
        System.out.println(OWL2NL_QRSimpleFormatter.compose("?,?,?",1,2,3));
    }*/
}
