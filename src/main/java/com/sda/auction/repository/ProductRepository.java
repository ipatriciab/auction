package com.sda.auction.repository;

import com.sda.auction.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>,
        PagingAndSortingRepository<Product, Integer> {

    @Query("select p from Product p where :now between p.startBiddingTime and p.endBiddingTime")
        // gives all the products which are in between start and end bidding time
    List<Product> findAllActive(@Param("now") LocalDateTime now);

    @Query("select p from Product p where :now < p.startBiddingTime ")
        // gives all the products which will be active in future
    List<Product> findAllFuture(@Param("now") LocalDateTime now);

    @Query("select distinct b.product from Bid b where :authenticatedUserEmail = b.user.email")
        // will show us only the products the logged in user bidded
    List<Product> findAllByBidder(@Param("authenticatedUserEmail") String authenticatedUserEmail);

    @Query("select p from Product p where :now > p.endBiddingTime and p.winner is null")
    List<Product> findAllExpiredAndUnassigned(@Param("now") LocalDateTime now);


    @Query("select p from Product p where (p.name LIKE %:keyword% or p.description LIKE %:keyword%) and :now between p.startBiddingTime and p.endBiddingTime ")
        // will search for all active products based on the name or description
    List<Product> search(@Param("keyword") String keyword, @Param("now") LocalDateTime now);

    @Query("select distinct b.product from Bid b where :authenticatedUserEmail = b.user.email " +
            "and :now between b.product.startBiddingTime and b.product.endBiddingTime")
    List<Product> findAllActiveByBidder(@Param("authenticatedUserEmail") String authenticatedUserEmail,
                                        @Param("now") LocalDateTime now);

    @Query("select p from Product p where :authenticatedUserEmail = p.winner.email " +
            "and :now > p.endBiddingTime ")
    List<Product> findAllExpiredAndAssigned(@Param("authenticatedUserEmail") String authenticatedUserEmail,
                                            @Param("now") LocalDateTime now);
}
