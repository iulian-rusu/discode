package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.Report
import com.discode.backend.business.models.ReportStatus
import com.discode.backend.business.models.UserIdentification
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ReportRowMapper : RowMapper<Report> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Report(
            messageId = rs.getLong("message_id"),
            message = rs.getString("message"),
            reported = UserIdentification(
                userId = rs.getLong("reported_id"),
                username = rs.getString("reported_username")
            ),
            reporter = UserIdentification(
                userId = rs.getLong("reporter_id"),
                username = rs.getString("reporter_username")
            ),
            date = rs.getTimestamp("report_date"),
            reason = rs.getString("report_reason"),
            status = ReportStatus.valueOf(rs.getString("status"))
        )
}