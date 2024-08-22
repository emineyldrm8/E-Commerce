package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    //product ekleneceği zaman cartentry sevrismde yer lacak,yine user,cartentry için yapılan her işlemde cart id ve user geer
    //user gerekect,ya da cart id.
    //cart entry bir şey eklemek için once uset ile cart oluşturulması gerekiyor.
    Optional<Cart> findByUserId(Long id);
    Optional<Cart> findByUser(User user);

}
