package com.lhw.apply.dao;

import com.lhw.apply.model.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：linhw
 * @date ：22.3.16 14:24
 * @description：
 * @modified By：
 */
@Repository
public interface SessionUserRepository extends JpaRepository<SessionUser,String> {

    SessionUser findByName(String name);

    List<SessionUser> findByAgeBetween(int start, int end);

}
