package com.jordyma.blink.global.config

import com.jordyma.blink.global.http.api.KakaoAuthApi
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit


@Configuration
class OpenApiConfig {

    @Value("\${open-api.kakao.open-key-url}")
    lateinit var kakaoOpenKeyUrl: String;

    private fun myConnectionProvider(): ConnectionProvider {
        return ConnectionProvider
            .builder("myConnectionProvider")
            .maxConnections(50) // 최대 연결 수
            .maxIdleTime(Duration.ofSeconds(60)) // 연결 유지 시간
            .build()
    }

    private fun httpClient(): HttpClient {
        return HttpClient.create(myConnectionProvider())
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
            .responseTimeout(Duration.ofMillis(50000))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS))
            }.resolver(DefaultAddressResolverGroup.INSTANCE)
    }

    @Bean
    fun kakaoAuthApi(): KakaoAuthApi {
        val webClient: WebClient = WebClient.builder()
            .baseUrl(kakaoOpenKeyUrl)
            .clientConnector(ReactorClientHttpConnector(httpClient()))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        return HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build()
            .createClient(KakaoAuthApi::class.java)
    }
}