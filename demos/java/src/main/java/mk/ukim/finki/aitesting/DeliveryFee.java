package mk.ukim.finki.aitesting;

public class DeliveryFee {
    public int calculate(int orderCents, boolean express) {
        if (orderCents < 1 || orderCents > 1_000_000) {
            throw new IllegalArgumentException("Order value outside range");
        }
        int fee = orderCents >= 5_000 ? 0 : 500;
        return fee + (express ? 300 : 0);
    }
}
