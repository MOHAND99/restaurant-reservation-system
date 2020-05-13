package restaurant;

import restaurant.user.Client;

public class ClientPayments {
    private final Client client;
    private final double payment;

    public ClientPayments(Client client, double payment) {
        this.client = client;
        this.payment = payment;
    }

    public Client getClient() {
        return client;
    }

    public double getPayment() {
        return payment;
    }
}
