package org.aksw.simba.owl2nl.qr.data;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRSimpleFormatter {
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
