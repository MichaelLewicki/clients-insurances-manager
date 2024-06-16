package cl.lewickidev;


import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {

    @Test
    void testInsuranceClientsByRUT() {
        Map<String, List<String>> result = Main.insuranceClientsByRUT();

        Map<String, List<String>> expectedData = new HashMap<>();
        expectedData.put("SEGURO DE VIDA", List.of("94020190", "86620855", "73826497", "7317855K"));
        expectedData.put("SEGURO APV", List.of("94020190", "99804238", "73826497", "7317855K"));
        expectedData.put("SEGURO COMPLEMENTARIO DE SALUD", List.of("94020190", "86620855", "73826497", "88587715", "7317855K"));

        assertEquals(expectedData.keySet(), result.keySet());
        assertEquals(expectedData.values().toString(), result.values().toString());
    }

    @Test
    void testHigherClientsBalances() {
        List<Integer> resultList = Main.higherClientsBalances();

        Boolean balanceValidator = true;
        for (Integer balance : resultList) {
            if (balance <= 30000) {
                balanceValidator = false;
            }
        }
        List<Integer> sortedlist = resultList.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        assertTrue(balanceValidator, "Hay balances menores a 30.000");
        assertEquals(sortedlist, resultList);
    }

    @Test
    void testInsuranceSortedByHighestBalance() {
        List<Integer> insuranceIds = Main.insuranceSortedByHighestBalance();

        assertEquals(3, insuranceIds.size());
        assertEquals(3, insuranceIds.stream().distinct().count());
        assertEquals(2, insuranceIds.get(0));
        assertEquals(1, insuranceIds.get(1));
        assertEquals(3, insuranceIds.get(2));
    }

}
