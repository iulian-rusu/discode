package com.discode.backend.persistence

import com.discode.backend.api.requests.ReportMessageRequest
import com.discode.backend.api.requests.UpdateReportsRequest
import com.discode.backend.business.models.Report
import com.discode.backend.business.models.ReportStatus
import com.discode.backend.persistence.mappers.ReportRowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReportRepository : RepositoryBase() {
    fun findReportsForMessage(messageId: Long): List<Report> =
        jdbcTemplate.query("SELECT * FROM message_reports WHERE message_id = ?", ReportRowMapper(), messageId)

    fun save(request: ReportMessageRequest): Report {
        jdbcTemplate.update(
            """
            INSERT INTO message_reports(message_id, reporter_id, report_date, report_reason, status)
            VALUES (?, ?, SYSDATE(), ?, ?)
        """, request.messageId, request.reporterId, request.reportReason, ReportStatus.PENDING.toString()
        )
        return Report(
            messageId = request.messageId,
            reporterId = request.reporterId,
            reportDate = Date(),
            reportReason = request.reportReason,
            status = ReportStatus.PENDING.toString()
        )
    }

    fun update(request: UpdateReportsRequest): List<Report> {
        jdbcTemplate.update(
            "UPDATE message_reports SET status = ? WHERE message_id = ?",
            request.status,
            request.messageId
        )
        return findReportsForMessage(request.messageId)
    }
}