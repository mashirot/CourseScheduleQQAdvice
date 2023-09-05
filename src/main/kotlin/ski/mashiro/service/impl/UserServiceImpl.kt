package ski.mashiro.service.impl

import ski.mashiro.constant.Consts
import ski.mashiro.dto.ApiCourseSearchDTO
import ski.mashiro.service.UserService
import ski.mashiro.util.HttpUtils

object UserServiceImpl: UserService {
    override fun verifyUser(username: String, apiToken: String): Boolean {
        val verifySearchDTO = ApiCourseSearchDTO(username, apiToken)
        val result = HttpUtils.sendReq(verifySearchDTO)
        return result.code == Consts.USER_LOGIN_SUCCESS
    }
}