package com.ozay.rowmapper;

import com.ozay.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class RoleMapper implements RowMapper {

    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        role.setBuildingId(rs.getLong("building_id"));
        role.setOrganizationUserRole(rs.getBoolean("organization_user_role"));
        role.setBelongTo(rs.getLong("belong_to"));
        role.setSortOrder(rs.getLong("sort_order"));

        return role;
    }
}
