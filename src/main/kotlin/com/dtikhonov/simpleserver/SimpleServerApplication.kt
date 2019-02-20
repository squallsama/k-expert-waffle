package com.dtikhonov.simpleserver

import com.dtikhonov.simpleserver.config.BlogProperties
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BlogProperties::class)
class SimpleServerApplication

fun main(args: Array<String>) {
	runApplication<SimpleServerApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}
