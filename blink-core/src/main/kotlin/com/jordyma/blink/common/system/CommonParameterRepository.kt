package com.jordyma.blink.common.system

import org.springframework.data.jpa.repository.JpaRepository

interface CommonParameterRepository : JpaRepository<CommonParameter, Long> {
    fun findByParamCode(paramCode: String): List<CommonParameter>
}