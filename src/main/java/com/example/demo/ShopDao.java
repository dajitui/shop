package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 2019/5/7.
 *
 * @author yangsen
 */
@Repository
public interface ShopDao extends JpaRepository<ShopDO, Long> {
}
