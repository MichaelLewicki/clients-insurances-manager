package cl.lewickidev;


import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {

    @Test
    void testInsuranceClientsByRUT() {
        Map<String, List<String>> expectedData = new HashMap<>();
        expectedData.put("SEGURO DE VIDA", List.of("94020190", "86620855", "73826497", "7317855K"));
        expectedData.put("SEGURO APV", List.of("94020190", "99804238", "73826497", "7317855K"));
        expectedData.put("SEGURO COMPLEMENTARIO DE SALUD", List.of("94020190", "86620855", "73826497", "88587715", "7317855K"));

        Map<String, List<String>> result = Main.insuranceClientsByRUT();

        assertEquals(expectedData.keySet(), result.keySet());
        assertEquals(expectedData.values().toString(), result.values().toString());
    }

    @Test
    void testHigherClientsBalances() {
        List<Integer> resultList = Main.higherClientsBalances();

        List<Integer> sortedlist = resultList.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        assertEquals(sortedlist, resultList);
        assertTrue(resultList.stream().allMatch(balance -> balance > 30000), "Hay balances menores a 30.000");
    }

    @Test
    void testInsuranceSortedByHighestBalance() {
        List<Integer> expectedOrder = List.of(2, 1, 3);

        List<Integer> insuranceIds = Main.insuranceSortedByHighestBalance();

        assertEquals(expectedOrder, insuranceIds, "Los IDs no están en el orden esperado");
    }

}
