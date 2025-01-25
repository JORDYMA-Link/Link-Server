package com.jordyma.blink.global.http.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class OpenKeyListResponse {

    @JsonProperty("keys")
    var keys: List<JWK>? = null

    @JsonCreator
    fun OpenKeyListResponse(@JsonProperty("keys") keys: List<JWK>?) {
        this.keys = keys
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class JWK {
        @JsonProperty("kid")
        val kid: String? = null

        @JsonProperty("kty")
        val kty: String? = null

        @JsonProperty("alg")
        val alg: String? = null

        @JsonProperty("use")
        val use: String? = null

        @JsonProperty("n")
        val n: String? = null

        @JsonProperty("e")
        val e: String? = null
    }
}