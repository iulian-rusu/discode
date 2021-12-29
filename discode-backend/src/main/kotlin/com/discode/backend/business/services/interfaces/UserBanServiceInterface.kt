package com.discode.backend.business.services.interfaces

import com.discode.backend.api.requests.BanUserRequest
import com.discode.backend.business.models.UserBan
import com.discode.backend.persistence.query.SearchUserBanQuery

interface UserBanServiceInterface {
    fun getAllBans(query: SearchUserBanQuery, authHeader: String?): List<UserBan>
    fun createBan(request: BanUserRequest, authHeader: String?): UserBan
    fun deleteBan(banId: Long, authHeader: String?): UserBan
}