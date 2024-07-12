package com.jordyma.blink.global.error.exception

import com.jordyma.blink.global.error.ErrorCode

class IdRequiredException(errorCode: ErrorCode) : IllegalStateException(errorCode.message)
