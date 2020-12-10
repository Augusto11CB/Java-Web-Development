package aug.bueno.c1.l5.orm.is.norm.entity;

import lombok.Data;

import java.util.List;

@Data
public class Customer {

    private int id;
    private String userName;

    // It would be better if the password field for the Customer class is char[ ] array instead of a String
    private String password;

//    private List<Order> customerOrders;
}
