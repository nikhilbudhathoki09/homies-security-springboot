package homiessecurity.dtos.khalti;


import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KhaltiInitiationRequest {

    private String return_url;
    private String website_url;
    private double amount;
    private String purchase_order_id;
    private String purchase_order_name;

    @JsonProperty("customer_info")
    private CustomerInfo customerInfo; // Change the field name to follow Java naming conventions

    public KhaltiInitiationRequest() {
    }

    public KhaltiInitiationRequest(String return_url, String website_url, double amount, String purchase_order_id, String purchase_order_name,
                                   CustomerInfo customerInfo) {
        this.return_url = return_url;
        this.website_url = website_url;
        this.amount = amount;
        this.purchase_order_id = purchase_order_id;
        this.purchase_order_name = purchase_order_name;
        this.customerInfo = customerInfo;
    }
}