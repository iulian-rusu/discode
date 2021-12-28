package com.discode.backend.business.services.interfaces

import com.discode.backend.api.requests.PostUserBanRequest
import com.discode.backend.business.models.UserBan
import com.discode.backend.persistence.query.SearchUserBanQuery

interface UserBanServiceInterface {
    fun getAllBans(query: SearchUserBanQuery, authHeader: String?): List<UserBan>
    fun createBan(request: PostUserBanRequest, authHeader: String?): UserBan
    fun deleteBan(banId: Long, authHeader: String?): UserBan
}