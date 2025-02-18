package org.test.order.application.controllers.order.list.response;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class ListAllOrdersResponse {
    private List<OrderResponseList> orders;

    public ListAllOrdersResponse(List<OrderResponseList> orders) {
        this.orders = orders;
    }

}