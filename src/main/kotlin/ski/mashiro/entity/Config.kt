package ski.mashiro.entity

class Config {
    var botQQ: Long = 12345L
    var ownerQQ: Long = 12345L
    var apiAddress: String = "http://127.0.0.1:8080"
    var whitelist: MutableSet<Long> = HashSet(10)
    var userMap: MutableMap<Long, User> = HashMap(10)
}