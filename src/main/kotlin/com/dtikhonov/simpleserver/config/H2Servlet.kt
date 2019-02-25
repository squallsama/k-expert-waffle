package com.dtikhonov.simpleserver.config

import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Configuration
import javax.servlet.ServletContext
import javax.servlet.ServletException

@Configuration
class H2Servlet : ServletContextInitializer {

    @Throws(ServletException::class)
    override fun onStartup(servletContext: ServletContext) {
        initH2Console(servletContext)
    }

    private fun initH2Console(servletContext: ServletContext) {
        log.info("Starting H2 console")
        val h2ConsoleServlet = servletContext.addServlet("H2Console", org.h2.server.web.WebServlet())
        h2ConsoleServlet.addMapping("/h2/*")
        h2ConsoleServlet.setLoadOnStartup(1)
    }

    companion object {
        private val log = LoggerFactory.getLogger(H2Servlet::class.java)
    }
}