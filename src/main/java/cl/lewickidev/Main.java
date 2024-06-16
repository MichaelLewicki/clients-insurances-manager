package cl.lewickidev;

import cl.lewickidev.dto.ClientWithBalance;
import cl.lewickidev.dto.InsuranceWithBalance;
import cl.lewickidev.model.Cliente;
import cl.lewickidev.model.Cuenta;
import cl.lewickidev.model.Seguro;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.out.println("[listClientsIds] " + listClientsIds().toString());
        System.out.println();
        System.out.println("[listClientsIdsSortedByRUT] " + listClientsIdsSortedByRUT().toString());
        System.out.println();
        System.out.println("[sortClientsTotalBalances] " + sortClientsTotalBalances().toString());
        System.out.println();
        System.out.println("[insuranceClientsByRUT] " + insuranceClientsByRUT().toString());
        System.out.println();
        System.out.println("[higherClientsBalances] " + higherClientsBalances().toString());
        System.out.println();
        System.out.println("[insuranceSortedByHighestBalance] " + insuranceSortedByHighestBalance().toString());
        System.out.println();
        System.out.println("[uniqueInsurance] " + uniqueInsurance().toString());
        System.out.println();
        System.out.println("[clientWithLessFunds] " + clientWithLessFunds().toString());
        System.out.println();
        System.out.println("[newClientRanking] --> " + newClientRanking());
    }

    private static final List<Cliente> clients = List.of(
            new Cliente(1, "86620855", "DANIEL BUSTOS"),
            new Cliente(2, "7317855K", "NICOLAS PEREZ"),
            new Cliente(3, "73826497", "ERNESTO GRANADO"),
            new Cliente(4, "88587715", "JORDAN MARTINEZ"),
            new Cliente(5, "94020190", "ALEJANDRO ZELADA"),
            new Cliente(6, "99804238", "DENIS ROJAS")
    );

    private static final List<Cuenta> accounts = List.of(
            new Cuenta(6, 1, 15000),
            new Cuenta(1, 3, 18000),
            new Cuenta(5, 3, 135000),
            new Cuenta(2, 2, 5600),
            new Cuenta(3, 1, 23000),
            new Cuenta(5, 2, 15000),
            new Cuenta(3, 3, 45900),
            new Cuenta(2, 3, 19000),
            new Cuenta(4, 3, 51000),
            new Cuenta(5, 1, 89000),
            new Cuenta(1, 2, 1600),
            new Cuenta(5, 3, 37500),
            new Cuenta(6, 1, 19200),
            new Cuenta(2, 3, 10000),
            new Cuenta(3, 2, 5400),
            new Cuenta(3, 1, 9000),
            new Cuenta(4, 3, 13500),
            new Cuenta(2, 1, 38200),
            new Cuenta(5, 2, 17000),
            new Cuenta(1, 3, 1000),
            new Cuenta(5, 2, 600),
            new Cuenta(6, 1, 16200),
            new Cuenta(2, 2, 10000)
    );

    private static final List<Seguro> insurances = List.of(
            new Seguro(1, "SEGURO APV"),
            new Seguro(2, "SEGURO DE VIDA"),
            new Seguro(3, "SEGURO COMPLEMENTARIO DE SALUD")
    );

    // Método para listar los IDs de clientes
    public static List<Integer> listClientsIds() {
        return clients.stream()
                .map(Cliente::getId)
                .toList();
    }

    // Método para listar los IDs de clientes ordenados por RUT
    public static List<Integer> listClientsIdsSortedByRUT() {
        return clients.stream()
                .sorted(Comparator.comparing(Cliente::getRut))
                .map(Cliente::getId)
                .toList();
    }

    // Método para listar los nombres de clientes
    // ordenados de mayor a menor por la suma TOTAL de los saldos
    // de cada cliente en los seguros que participa
    public static List<String> sortClientsTotalBalances() {
        return clients.stream()
                .map(client -> {
                    Double totalBalance = accounts.stream()
                            .filter(account -> account.getClientId() == client.getId())
                            .mapToDouble(Cuenta::getBalance)
                            .sum();
                    return new ClientWithBalance(client, totalBalance);
                })
                .sorted(Comparator.comparing(ClientWithBalance::getTotalBalance).reversed())
                .map(clientWithBalance -> clientWithBalance.getClient().getName())
                .toList();
    }

    // Método para generar un objeto en que las claves sean los nombres de los seguros
    // y los valores un arreglo con los RUTs de sus clientes ordenados alfabéticamente por nombre
    public static Map<String, List<String>> insuranceClientsByRUT() {

        Map<Integer, Set<Cliente>> clientsGroupedByInsuranceId = getClientsGroupedByInsuranceId();

        return getAlphabeticallySortedClientRUTs(clientsGroupedByInsuranceId);
    }

    private static Map<Integer, Set<Cliente>> getClientsGroupedByInsuranceId() {
        Map<Integer, Set<Cliente>> insuranceIdWithClients = new HashMap<>();
        for (Cuenta account : accounts) {
            Integer insuranceId = account.getInsuranceId();
            Integer clientId = account.getClientId();

            Cliente client = clients.stream()
                    .filter(c -> c.getId() == clientId)
                    .findFirst()
                    .orElse(null);

            if (client != null) {
                insuranceIdWithClients
                        .computeIfAbsent(insuranceId, newKey -> new HashSet<>())
                        .add(client);
            }
        }
        return insuranceIdWithClients;
    }

    private static Map<String, List<String>> getAlphabeticallySortedClientRUTs(Map<Integer, Set<Cliente>> insuranceIdWithClients) {
        Map<String, List<String>> sortedRutsAlphabetically = new HashMap<>();
        for (Seguro insurance : insurances) {
            Set<Cliente> clientSet = insuranceIdWithClients.get(insurance.getId());

            List<Cliente> clientList = new ArrayList<>(clientSet);
            clientList.sort(Comparator.comparing(Cliente::getName));

            List<String> ruts = clientList.stream()
                    .map(Cliente::getRut)
                    .toList();

            sortedRutsAlphabetically.put(insurance.getName(), ruts);
        }
        return sortedRutsAlphabetically;
    }

    // Método para generar un arreglo ordenado decrecientemente
    // con los saldos de clientes que tengan más de 30.000 en el "Seguro APV"
    public static List<Integer> higherClientsBalances() {
        return accounts.stream()
                .filter(account -> account.getBalance() > 30000 && account.getInsuranceId() == 1)
                .map(Cuenta::getBalance)
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    // Método para generar un arreglo con IDs de los seguros ordenados crecientemente
    // por la cantidad TOTAL de dinero que administran
    public static List<Integer> insuranceSortedByHighestBalance() {
        return insurances.stream()
                .map(insurance -> {
                    Integer totalBalance = accounts.stream()
                            .filter(account -> account.getInsuranceId() == insurance.getId())
                            .mapToInt(Cuenta::getBalance)
                            .sum();
                    return new InsuranceWithBalance(insurance.getId(), totalBalance);
                })
                .sorted(Comparator.comparing(InsuranceWithBalance::getBalance))
                .map(InsuranceWithBalance::getInsuranceId)
                .toList();
    }

    // Método para generar un objeto en que las claves sean los nombres de los Seguros
    // y los valores el número de clientes que solo tengan cuentas en ese seguro
    public static Map<String, Long> uniqueInsurance() {
        Map<Integer, List<Cuenta>> accountsByClient = accounts.stream()
                .collect(Collectors.groupingBy(Cuenta::getClientId));

        Map<String, Long> result = new HashMap<>();
        for (Seguro insurance : insurances) {
            Integer insuranceId = insurance.getId();
            String insuranceName = insurance.getName();
            Long count = clients.stream()
                    .filter(client -> {
                        List<Cuenta> accountsClient = accountsByClient.get(client.getId());
                        return !accountsClient.isEmpty() && accountsClient.stream()
                                .allMatch(account -> account.getInsuranceId() == insuranceId);
                    })
                    .count();
            result.put(insuranceName, count);
        }
        return result;
    }

    // Método para generar un objeto en que las claves sean los nombres de los Seguros
    // y los valores el ID de su cliente con menos fondos
    public static Map<String, Integer> clientWithLessFunds() {
        Map<String, Integer> result = new HashMap<>();

        for (Seguro insurance : insurances) {
            Map<Integer, Integer> clientTotalBalance = accounts.stream()
                    .filter(account -> account.getInsuranceId() == insurance.getId())
                    .collect(Collectors.groupingBy(
                            Cuenta::getClientId,
                            Collectors.summingInt(Cuenta::getBalance)
                    ));

            if (!clientTotalBalance.isEmpty()) {
                Integer clientWithLessFunds = Collections.min(
                        clientTotalBalance.entrySet(),
                        Map.Entry.comparingByValue()
                ).getKey();
                result.put(insurance.getName(), clientWithLessFunds);
            }
        }

        return result;
    }

    // Método para agregar un nuevo cliente con datos ficticios
    // y una cuenta en el "SEGURO COMPLEMENTARIO DE SALUD" con un saldo de 15000 para este nuevo cliente,
    // luego devolver el lugar que ocupa este cliente en el ranking de la pregunta 2
    public static int newClientRanking() {
        Integer newClientId = clients.size() + 1;

        Cliente newClient = new Cliente(newClientId, "83452982", "ALEXANDER RIVAS");
        List<Cliente> updatedClients = new ArrayList<>(clients);
        updatedClients.add(newClient);

        Cuenta newAccount = new Cuenta(newClientId, 3, 64350);
        List<Cuenta> updatedAccounts = new ArrayList<>(accounts);
        updatedAccounts.add(newAccount);

        List<Integer> sortedClientIds = updatedClients.stream()
                .sorted(Comparator.comparing(Cliente::getRut))
                .map(Cliente::getId)
                .toList();
        System.out.println("[newClientRanking] newSortedClientIds --> " + sortedClientIds);
        return sortedClientIds.indexOf(newClientId) + 1;
    }





}