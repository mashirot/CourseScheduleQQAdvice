package ski.mashiro.service

interface UserService {
    fun verifyUser(username: String, apiToken: String): Boolean
}