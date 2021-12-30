package com.discode.backend.business.services

import com.discode.backend.api.requests.BanUserRequest
import com.discode.backend.business.models.UserBan
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.business.services.interfaces.UserBanServiceInterface
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.UserBanRepository
import com.discode.backend.persistence.UserRepository
import com.discode.backend.persistence.mappers.UserBanRowMapper
import com.discode.backend.persistence.query.SearchUserBanQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserBanService: JwtAuthorized(), UserBanServiceInterface {
    @Autowired
    private lateinit var userBanRepository: UserBanRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var genericRepository: GenericQueryRepository

    override fun getAllBans(query: SearchUserBanQuery, authHeader: String?): List<UserBan> {
        return ifAdmin(authHeader) {
            genericRepository.find(query, UserBanRowMapper())
        }
    }

    override fun createBan(request: BanUserRequest, authHeader: String?): UserBan {
        return ifAdmin(authHeader) {
            if (userRepository.isAdmin(request.userId)) {
                throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Cannot ban admin user")
            }
            userBanRepository.save(request)
        }
    }

    override fun deleteBan(banId: Long, authHeader: String?): UserBan {
        return ifAdmin(authHeader) {
            userBanRepository.deleteOne(banId)
        }
    }
}