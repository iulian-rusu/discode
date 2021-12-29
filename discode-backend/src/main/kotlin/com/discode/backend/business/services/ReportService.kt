package com.discode.backend.business.services

import com.discode.backend.api.requests.ReportMessageRequest
import com.discode.backend.api.requests.UpdateReportsRequest
import com.discode.backend.business.models.Report
import com.discode.backend.business.models.ReportStatus
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.business.services.interfaces.ReportServiceInterface
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.ReportRepository
import com.discode.backend.persistence.mappers.ReportRowMapper
import com.discode.backend.persistence.query.SearchReportQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ReportService : JwtAuthorized(), ReportServiceInterface {
    @Autowired
    private lateinit var reportRepository: ReportRepository

    @Autowired
    private lateinit var genericRepository: GenericQueryRepository

    override fun getAllReports(query: SearchReportQuery, authHeader: String?): List<Report> {
        return ifAdmin(authHeader) {
            genericRepository.find(query, ReportRowMapper())
        }
    }

    override fun createReport(request: ReportMessageRequest, authHeader: String?): Report {
        return ifAuthorizedAs(request.reporterId, authHeader) {
            reportRepository.save(request)
        }
    }

    override fun updateReports(request: UpdateReportsRequest, authHeader: String?): List<Report> {
        if (request.status !in ReportStatus.values().map { it.toString() })
            throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid report status: '${request.status}'")
        return ifAdmin(authHeader) {
            reportRepository.update(request)
        }
    }
}