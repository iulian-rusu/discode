package com.discode.backend.api.controllers

import com.discode.backend.api.requests.ReportMessageRequest
import com.discode.backend.api.requests.UpdateReportsRequest
import com.discode.backend.api.utils.HttpResponse
import com.discode.backend.api.utils.ScopeGuarded
import com.discode.backend.business.models.Report
import com.discode.backend.business.services.interfaces.ReportServiceInterface
import com.discode.backend.persistence.query.SearchReportQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
class ReportController : ScopeGuarded(ReportController::class) {
    @Autowired
    private lateinit var reportService: ReportServiceInterface

    @GetMapping("")
    fun getReports(
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<Report>> {
        return guardedWith(HttpStatus.BAD_REQUEST, HttpResponse.GENERIC_ERROR_MESSAGE) {
            ResponseEntity.ok(reportService.getAllReports(SearchReportQuery(searchParams), authHeader))
        }
    }

    @PostMapping("")
    fun postReport(
        @RequestBody request: ReportMessageRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Report> {
        return guardedWith(HttpStatus.NOT_ACCEPTABLE, "Cannot create report - possible duplicate") {
            HttpResponse.created(reportService.createReport(request, authHeader))
        }
    }

    @PatchMapping("")
    fun patchReports(
        @RequestBody request: UpdateReportsRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<Report>> {
        return guardedWith(HttpStatus.CONFLICT, "Cannot update reports") {
            ResponseEntity.ok(reportService.updateReports(request, authHeader))
        }
    }

    @PutMapping("")
    fun putReports(
        @RequestBody request: UpdateReportsRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<Report>> {
        return guardedWith(HttpStatus.CONFLICT, "Cannot update reports") {
            ResponseEntity.ok(reportService.updateReports(request, authHeader))
        }
    }
}
