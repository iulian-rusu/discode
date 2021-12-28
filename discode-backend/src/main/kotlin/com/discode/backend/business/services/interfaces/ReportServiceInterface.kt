package com.discode.backend.business.services.interfaces

import com.discode.backend.api.requests.PostReportRequest
import com.discode.backend.api.requests.UpdateReportsRequest
import com.discode.backend.business.models.Report
import com.discode.backend.persistence.query.SearchReportQuery

interface ReportServiceInterface {
    fun getAllReports(query: SearchReportQuery, authHeader: String?): List<Report>
    fun createReport(request: PostReportRequest, authHeader: String?): Report
    fun updateReports(request: UpdateReportsRequest, authHeader: String?): List<Report>
}