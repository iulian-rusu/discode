package com.discode.backend.persistence

import com.discode.backend.api.requests.ReportMessageRequest
import com.discode.backend.api.requests.UpdateReportsRequest
import com.discode.backend.business.models.Report
import com.discode.backend.business.models.ReportStatus
import com.discode.backend.persistence.mappers.ReportRowMapper
import com.discode.backend.persistence.query.SearchReportQuery
import org.springframework.stereotype.Repository

@Repository
class ReportRepository : RepositoryBase() {
    fun findReportsForMessage(messageId: Long): List<Report> {
        val query = SearchReportQuery(hashMapOf("message_id" to messageId.toString()))
        return namedJdbcTemplate.query(query.getSql(), query.params, ReportRowMapper())
    }

    fun save(request: ReportMessageRequest): Report {
        jdbcTemplate.update(
            """
            INSERT INTO message_reports(message_id, reporter_id, report_date, report_reason, status)
            VALUES (?, ?, SYSDATE(), ?, ?)
        """, request.messageId, request.reporterId, request.reportReason, ReportStatus.PENDING.toString()
        )
        val query = SearchReportQuery(
            hashMapOf(
                "message_id" to request.messageId.toString(),
                "reporter_id" to request.reporterId.toString()
            )
        )
        return namedJdbcTemplate.query(query.getSql(), query.params, ReportRowMapper()).first()
    }

    fun update(request: UpdateReportsRequest): List<Report> {
        jdbcTemplate.update(
            "UPDATE message_reports SET status = ? WHERE message_id = ?",
            request.status.toString(),
            request.messageId
        )
        return findReportsForMessage(request.messageId)
    }
}