package com.jianlang.login.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.jianlang.common.web.app.security")
public class SecurityConfig {
}
