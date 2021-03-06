package com.ozay.repository;

import com.ozay.model.Role;
import com.ozay.rowmapper.RoleMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class RoleMemberRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void create(long roleId, long memberId){
        String query="INSERT INTO role_member (role_id, member_id) VALUES(:roleId, :memberId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        params.addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(long roleId, long memberId){
        String query="DELETE FROM role_member WHERE role_id =:roleId AND member_id=:memberId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        params.addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(query,params);
    }

}
