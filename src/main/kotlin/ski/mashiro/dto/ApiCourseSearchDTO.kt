package ski.mashiro.dto

/**
 * @author MashiroT
 */
class ApiCourseSearchDTO {

    var username: String
    var apiToken: String
    var dayOfWeek: String? = null
    var isEffective: Boolean? = null

    constructor(username: String, apiToken: String) {
        this.username = username
        this.apiToken = apiToken
    }

    constructor(username: String, apiToken: String, isEffective: Boolean) {
        this.username = username
        this.apiToken = apiToken
        this.isEffective = isEffective
    }

    constructor(username: String, apiToken: String, dayOfWeek: String, isEffective: Boolean) {
        this.username = username
        this.apiToken = apiToken
        this.dayOfWeek = dayOfWeek
        this.isEffective = isEffective
    }

}
