package com.discode.backend.persistence.mappers

import com.discode.backend.models.User
import com.discode.backend.models.UserProfile
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class UserRowMapper : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        User(
            username = rs.getString("username"),
            profile = UserProfile(
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                email = rs.getString("email"),
                description = rs.getString("description"),
                imagePath = rs.getString("image_path")
            )
        )
}