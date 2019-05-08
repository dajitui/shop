package com.example.demo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created on 2019/5/7.
 *
 * @author yangsen
 */
@Entity
@Table(name = "shop")
@Data
@ToString
public class ShopDO {

    @Id
    @Column(name = "Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column()
    private String name;

    @Column()
    private Integer point;

}
