package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.Report
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ReportRowMapper : RowMapper<Report> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Report(
            messageId = rs.getLong("message_id"),
            reporterId = rs.getLong("reporter_id"),
            reportDate = rs.getTimestamp("report_date"),
            reportReason = rs.getString("report_reason"),
            status = rs.getString("status")
        )
}