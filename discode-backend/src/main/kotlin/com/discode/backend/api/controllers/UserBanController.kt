package com.discode.backend.api.controllers

import com.discode.backend.api.requests.BanUserRequest
import com.discode.backend.api.utils.HttpResponse
import com.discode.backend.api.utils.ScopeGuarded
import com.discode.backend.business.models.UserBan
import com.discode.backend.business.services.interfaces.UserBanServiceInterface
import com.discode.backend.persistence.query.SearchUserBanQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bans")
class UserBanController : ScopeGuarded(UserBanController::class) {
    @Autowired
    private lateinit var userBanService: UserBanServiceInterface

    @GetMapping("")
    fun getAllBans(
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<UserBan>> {
        return guardedWith(HttpStatus.BAD_REQUEST, HttpResponse.GENERIC_ERROR_MESSAGE) {
            ResponseEntity.ok(userBanService.getAllBans(SearchUserBanQuery(searchParams), authHeader))
        }
    }

    @PostMapping("")
    fun postBan(
        @RequestBody request: BanUserRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<UserBan> {
        return guardedWith(HttpStatus.CONFLICT, "Cannot create requested ban entry") {
            HttpResponse.created(userBanService.createBan(request, authHeader))
        }
    }

    @DeleteMapping("/{banId}")
    fun deleteBan(
        @PathVariable banId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<UserBan> {
        return guardedWith(HttpStatus.NOT_FOUND, "Cannot find user ban record") {
            ResponseEntity.ok(userBanService.deleteBan(banId, authHeader))
        }
    }
}