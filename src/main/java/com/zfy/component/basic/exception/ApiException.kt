package com.zfy.component.basic.exception

/**
 * CreateAt : 2018/9/9
 * Describe :
 *
 * @author chendong
 */
class RequestException(val code: Int) : IllegalStateException() {
    companion object {
        const val ERROR_NETWORK = 101
    }
}