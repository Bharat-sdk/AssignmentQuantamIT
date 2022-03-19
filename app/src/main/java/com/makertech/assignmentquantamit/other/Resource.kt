package com.makertech.assignmentquantamit.other

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Status.ERROR, data, message)

        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data, message = null)
    }
}
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}