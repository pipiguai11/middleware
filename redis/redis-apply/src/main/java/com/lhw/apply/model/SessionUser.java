package com.lhw.apply.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author ：linhw
 * @date ：22.3.16 14:21
 * @description：用户会话
 * @modified By：
 */
@Data
@Entity
@Table
public class SessionUser implements Serializable {

    @Id
    @Column(name = "userId")
    private String userId = UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

}
