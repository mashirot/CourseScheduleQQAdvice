package ski.mashiro.common

/**
 * @param code 状态码
 * @param data 返回数据
 * @param msg  响应消息
 * @param <T>  数据类型
 * @author MashiroT
 * @since 2022-06-28
</T> */
data class Result<T>(val code: Int, val data: T, val msg: String?) {
    companion object {
        fun <T> success(code: Int, data: T): Result<T> {
            return Result(code, data, null)
        }

        fun <T> failed(code: Int, msg: String?): Result<T?> {
            return Result(code, null, msg)
        }
    }
}
