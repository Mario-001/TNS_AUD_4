package mk.ukim.finki.aitesting;

import mk.ukim.finki.aitesting.Ports.*;

public class CarrierAdapter implements LabelGateway {
    private final CarrierSdk sdk;
    public CarrierAdapter(CarrierSdk sdk) { this.sdk = sdk; }
    public String createLabel(String customer) {
        return sdk.account("teaching").labels().create(customer);
    }
}
