package com.abnamro.recipes;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Challenge {

    public static String dnaComplement(final String dna) {
        final StringBuilder complement = new StringBuilder();
        for (final char nucleotide : dna.toCharArray()) {
            switch (nucleotide) {
                case 'A' -> complement.append('T');
                case 'T' -> complement.append('A');
                case 'C' -> complement.append('G');
                case 'G' -> complement.append('C');
            }
        }
        return complement.toString();
    }

    public static String fizzBuzz(final int n) {
        if (n < 1) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                sb.append("FizzBuzz");
            } else if (i % 3 == 0) {
                sb.append("Fizz");
            } else if (i % 5 == 0) {
                sb.append("Buzz");
            } else {
                sb.append(i);
            }

            if (i != n) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String fizzBuzzz(final int n) {
        if (n < 1) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= n; i++) {
            String result = "";

            if (i % 3 == 0) {
                result = "Fizz";
            }

            if (i % 5 == 0) {
                result += "Buzz";
            }

            if (result.isEmpty()) {
                result = String.valueOf(i);
            }

            sb.append(result);

            if (i < n) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public static int streams(final Integer number) {
        if (number == null || number <= 0) {
            return 0;
        }

        return IntStream.range(1, number)
                .filter(n -> n % 3 == 0)
                .sum();
    }


    public static class Customer {
        private final String firstName;
        private final String lastName;
        private final int age;

        public Customer(final String firstName, final String lastName, final int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        public String getFirstName() {
            return this.firstName;
        }

        public String getLastName() {
            return this.lastName;
        }

        public int getAge() {
            return this.age;
        }
    }

    public static List<String> getAllNames(final List<Customer> customers, final int minAge) {
        return customers.stream()
                .filter(c -> c.getAge() >= minAge)
                .sorted(Comparator.comparing(Customer::getLastName)
                        .thenComparing(Customer::getFirstName))
                .map(c -> String.format("%s, %s", c.getLastName(), c.getFirstName()))
                .collect(Collectors.toList());
    }
}