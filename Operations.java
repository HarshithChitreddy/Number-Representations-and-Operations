package cs250.hw1;

public class Operations {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Task 1\nIncorrect number of arguments have been provided. Program Terminating!");
            return;
        } else {
            System.out.println("Task 1\nCorrect number of arguments given.");
        }

        String[] types = new String[3];
        boolean[] valid = new boolean[3];
        int[] decimals = new int[3];

        identifyNumberSystem(args, types);
        if (!validateNumbers(args, types, valid)) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            decimals[i] = convertToDecimal(args[i], types[i]);
        }

        printConversions(args, decimals);
        printComplements(args, decimals);
        performBitwiseOperations(args, decimals);
        performBitwiseShifts(decimals);
    }

    private static void identifyNumberSystem(String[] args, String[] types) {
        System.out.println("Task 2");
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("0b")) {
                types[i] = "Binary";
            } else if (args[i].startsWith("0x")) {
                types[i] = "Hexadecimal";
            } else {
                types[i] = "Decimal";
            }
            System.out.println(args[i] + "=" + types[i]);
        }
    }

    private static boolean validateNumbers(String[] args, String[] types, boolean[] valid) {
        System.out.println("Task 3");
        boolean allValid = true;
        for (int i = 0; i < args.length; i++) {
            switch (types[i]) {
                case "Binary":
                    valid[i] = args[i].substring(2).matches("[01]+");
                    break;
                case "Hexadecimal":
                    valid[i] = args[i].substring(2).matches("[0-9A-Fa-f]+");
                    break;
                case "Decimal":
                    valid[i] = args[i].matches("\\d+");
                    break;
            }
            System.out.println(args[i] + "=" + (valid[i] ? "true" : "false"));
            allValid &= valid[i];
        }
        return allValid;
    }

    private static int convertToDecimal(String number, String type) {
        switch (type) {
            case "Decimal":
                return stringToDecimal(number);
            case "Binary":
                return binaryToDecimal(number.substring(2));
            case "Hexadecimal":
                return hexadecimalToDecimal(number.substring(2));
        }
        return -1;
    }

    private static int stringToDecimal(String number) {
        int decimal = 0;
        for (int i = 0; i < number.length(); i++) {
            decimal = decimal * 10 + (number.charAt(i) - '0');
        }
        return decimal;
    }

    private static int binaryToDecimal(String binary) {
        int decimal = 0;
        int base = 1;
        for (int i = binary.length() - 1; i >= 0; i--) {
            decimal += (binary.charAt(i) - '0') * base;
            base *= 2;
        }
        return decimal;
    }

    private static int hexadecimalToDecimal(String hex) {
        int decimal = 0;
        int base = 1;
        for (int i = hex.length() - 1; i >= 0; i--) {
            char ch = hex.charAt(i);
            int value;
            if (ch >= '0' && ch <= '9') {
                value = ch - '0';
            } else if (ch >= 'A' && ch <= 'F') {
                value = ch - 'A' + 10;
            } else {
                value = ch - 'a' + 10;
            }
            decimal += value * base;
            base *= 16;
        }
        return decimal;
    }

    private static void printConversions(String[] args, int[] decimals) {
        System.out.println("Task 4");
        for (int i = 0; i < decimals.length; i++) {
            String binary = decimalToBinary(decimals[i]);
            String hex = decimalToHexadecimal(decimals[i]);
            System.out.printf("Start=%s,Binary=0b%s,Decimal=%d,Hexadecimal=0x%s\n", args[i], binary, decimals[i], hex);
        }
    }

    private static String decimalToBinary(int decimal) {
        if (decimal == 0) {
            return "0";
        }
        StringBuilder binary = new StringBuilder();
        while (decimal > 0) {
            binary.insert(0, (decimal % 2));
            decimal = decimal / 2;
        }
        return binary.toString();
    }

    private static String decimalToHexadecimal(int decimal) {
        if (decimal == 0) {
            return "0";
        }
        StringBuilder hex = new StringBuilder();
        char[] hexDigits = "0123456789abcdef".toCharArray();
        while (decimal > 0) {
            hex.insert(0, hexDigits[decimal % 16]);
            decimal = decimal / 16;
        }
        return hex.toString();
    }

    private static void printComplements(String[] args, int[] decimals) {
        System.out.println("Task 5");
        for (int i = 0; i < decimals.length; i++) {
            String binary = decimalToBinary(decimals[i]);
            String paddedBinary = String.format("%32s", binary).replace(' ', '0');
            String onesComp = onesComplement(paddedBinary);
            System.out.printf("%s=%s=>%s\n", args[i], binary, onesComp.substring(onesComp.length() - binary.length()));
        }
        System.out.println("Task 6");
        for (int i = 0; i < decimals.length; i++) {
            String binary = decimalToBinary(decimals[i]);
            String paddedBinary = String.format("%32s", binary).replace(' ', '0');
            String twosComp = twosComplement(paddedBinary);
            System.out.printf("%s=%s=>%s\n", args[i], binary, twosComp.substring(twosComp.length() - binary.length()));
        }
    }

    private static void performBitwiseOperations(String[] args, int[] decimals) {
        int or = 0, and = ~0, xor = 0;
        for (int decimal : decimals) {
            or |= decimal;
            and &= decimal;
            xor ^= decimal;
        }
        System.out.println("Task 7");

        String binaryOr = decimalToBinary(or);
        String binaryAnd = padBinaryString(decimalToBinary(and), Math.max(decimalToBinary(decimals[0]).length(),
                Math.max(decimalToBinary(decimals[1]).length(), decimalToBinary(decimals[2]).length())));
        String binaryXor = decimalToBinary(xor);

        String binaryArg0 = decimalToBinary(decimals[0]);
        String binaryArg1 = decimalToBinary(decimals[1]);
        String binaryArg2 = decimalToBinary(decimals[2]);

        System.out.printf("%s|%s|%s=%s\n", binaryArg0, binaryArg1, binaryArg2, binaryOr);
        System.out.printf("%s&%s&%s=%s\n", binaryArg0, binaryArg1, binaryArg2, binaryAnd);
        System.out.printf("%s^%s^%s=%s\n", binaryArg0, binaryArg1, binaryArg2, binaryXor);
    }

    private static void performBitwiseShifts(int[] decimals) {
        System.out.println("Task 8");
        for (int decimal : decimals) {
            String binary = decimalToBinary(decimal);
            String leftShifted = binary + "00";
            String rightShifted = binary.length() > 2 ? binary.substring(0, binary.length() - 2) : "0";
            System.out.printf("%s<<2=%s,%s>>2=%s\n", binary, leftShifted, binary, rightShifted);
        }
    }

    private static String onesComplement(String binary) {
        StringBuilder result = new StringBuilder();
        for (char c : binary.toCharArray()) {
            result.append(c == '0' ? '1' : '0');
        }
        return result.toString();
    }

    private static String twosComplement(String binary) {
        StringBuilder onesComp = new StringBuilder(onesComplement(binary));
        boolean carry = true;
        for (int i = onesComp.length() - 1; i >= 0; i--) {
            if (carry) {
                if (onesComp.charAt(i) == '1') {
                    onesComp.setCharAt(i, '0');
                } else {
                    onesComp.setCharAt(i, '1');
                    carry = false;
                }
            }
        }
        return onesComp.toString();
    }

    private static String padBinaryString(String binary, int minLength) {
        if (binary.length() < minLength) {
            return String.format("%" + minLength + "s", binary).replace(' ', '0');
        }
        return binary;
    }
}