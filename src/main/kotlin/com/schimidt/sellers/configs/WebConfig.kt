package com.schimidt.sellers.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.util.UrlPathHelper

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    @Bean
    fun shallowEtagHeaderFilter(): ShallowEtagHeaderFilter {
        return ShallowEtagHeaderFilter()
    }

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        val urlPathHelper = UrlPathHelper()
        urlPathHelper.setRemoveSemicolonContent(false)
        configurer.setUrlPathHelper(urlPathHelper)
        super.configurePathMatch(configurer)
    }
}

