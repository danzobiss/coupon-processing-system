package com.danzobiss.couponprocessing.util;

public class Util {

    public static boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calculateDigit(cnpj, pesos1);
        int digito2 = calculateDigit(cnpj, pesos2);

        return digito1 == cnpj.charAt(12) - '0' && digito2 == cnpj.charAt(13) - '0';

    }

    private static int calculateDigit(String cnpj, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos[i];
        }
        int resultado = soma % 11;
        return resultado < 2 ? 0 : 11 - resultado;
    }


}
