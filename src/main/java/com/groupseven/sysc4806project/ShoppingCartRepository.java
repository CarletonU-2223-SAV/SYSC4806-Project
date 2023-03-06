package com.groupseven.sysc4806project;

import org.springframework.data.repository.CrudRepository;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Integer>{
    ShoppingCart findByCustomer(User user);
}
