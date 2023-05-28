//package org.nextlevel.security;
//
//import org.nextlevel.security.jwt.JwtAuthenticationEntryPoint;
//import org.nextlevel.security.jwt.JwtRequestFilter;
//import org.nextlevel.security.oauth.CustomOAuth2UserService;
//import org.nextlevel.security.oauth.OAuthLoginSuccessHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig2 extends WebSecurityConfigurerAdapter {
////			.antMatchers("/", "/login", "/sms", "/smscallback", "/home", "/js/**", "/css/**", "/icons/**", "/icons/fonts/**", "/v3/api-docs", "/api-docs", "/swagger-ui.html").permitAll()
//	@Autowired
//	DataSource dataSource;
//	@Autowired
//	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
////	@Autowired
////	private UserDetailsService jwtUserDetailsService;
//	@Autowired
//	private JwtRequestFilter jwtRequestFilter;
//	@Autowired
//	private UserDetailsService jwtUserDetailsService;
//	private static final String[] AUTH_WHITELIST = {
//			"/authenticate",
//			"/api/authenticate",
//			"/api/register",
//			"/swagger-resources/**",
//			"/swagger-ui/**",
//			"/v3/api-docs",
//			"/webjars/**",
//			"/",
//			"/login",
//			"/sms",
//			"/smscallback",
//			"/home",
//			"/js/**",
//			"/css/**",
//			"/icons/**",
//			"/icons/fonts/**",
//			"/v3/api-docs",
//			"/api-docs",
//			"/swagger-ui.html"
//	};
//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new UserDetailsServiceImpl();
//	}
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public LogoutSuccessHandler logoutSuccessHandler() {
//		return new CustomLogoutSuccessHandler();
//	}
//
//	@Bean
//	public AccessDeniedHandler accessDeniedHandler() {
//		return new CustomAccessDeniedHandler();
//	}
//
//	@Bean
//	public AuthenticationFailureHandler authenticationFailureHandler() {
//		return new CustomAuthenticationFailureHandler();
//	}
//
////	@Override
////	@Bean
////	public AuthenticationManager authenticationManagerBean() throws Exception {
////		return super.authenticationManagerBean();
////	}
//
//	//Enable jdbc authentication
//	@Autowired
//	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource);
//	}
//
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//
//		return authProvider;
//	}
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
//	}
//
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	//TODO::Disable CORS - START
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		// configure AuthenticationManager so that it knows from where to load
//		// user for matching credentials
//		// Use BCryptPasswordEncoder
//		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
//	}
//
////	@Bean
////	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////		http.authorizeRequests()
////				.anyRequest()
////				.authenticated()
////				.and()
////				.httpBasic()
////				.and()
////				.cors();
////		return http.build();
////	}
//
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("*");
//			}
//		};
//	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//		return source;
//	}
//
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/*").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE", "HEAD")
//				.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
//						"Access-Control-Request-Headers")
//				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//				.allowCredentials(true).maxAge(360000);
//	}
//
//	//TODO::Disable CORS - END
//
//	@Override
//	protected void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
//
//		httpSecurity
//			.csrf().disable()
//			.authorizeRequests()
//			.antMatchers(AUTH_WHITELIST).permitAll()
//
//			//admin
//			.antMatchers("/admin/**")
//				.hasAuthority("NEXTLEVEL_SUPER_ADMIN")
//
//			//orgs
//			.antMatchers(HttpMethod.POST, "/api/v1/orgs/{orgId}/schools/bulkupload")
//				//.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "ORG_ADMIN")
//				.access("@orgSecurity.hasUserIdSpecificOrg(authentication, #orgId)")
//			.antMatchers(HttpMethod.POST, "/api/v1/orgs/**")
//				.hasAuthority("NEXTLEVEL_SUPER_ADMIN")
//			.antMatchers(HttpMethod.DELETE, "/api/v1/orgs/**")
//				.hasAuthority("NEXTLEVEL_SUPER_ADMIN")
//			.antMatchers(HttpMethod.DELETE, "/api/v2/orgs/**")
//				.hasAuthority("NEXTLEVEL_SUPER_ADMIN")
//			.antMatchers(HttpMethod.PUT, "/api/v1/orgs/**")
//				.hasAuthority("NEXTLEVEL_SUPER_ADMIN")
//			.antMatchers(HttpMethod.GET, "/api/v1/orgs/{orgId}")
//				.access("@orgSecurity.hasUserIdSpecificOrg(authentication, #orgId)")
//			.antMatchers(HttpMethod.GET, "/api/v1/orgs/{orgId}/schools")
//				.access("@orgSecurity.hasUserIdSpecificOrg(authentication, #orgId)")
//			.antMatchers(HttpMethod.GET, "/api/v1/orgs/**")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "NEXTLEVEL_USER")
//
//			//schools
//			.antMatchers(HttpMethod.POST, "/api/v1/schools")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "ORG_ADMIN")
//			.antMatchers(HttpMethod.DELETE, "/api/v1/schools/**")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "ORG_ADMIN")
//			.antMatchers(HttpMethod.DELETE, "/api/v2/schools/**")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "ORG_ADMIN")
//			.antMatchers(HttpMethod.PUT, "/api/v1/schools/**")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "ORG_ADMIN")
//			.antMatchers(HttpMethod.GET, "/api/v1/schools/{schoolId}")
//				//.access("@schoolSecurity.hasUserId(authentication, #schoolId)") //TODO:: specific school access
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "NEXTLEVEL_USER", "ORG_ADMIN")
//			.antMatchers(HttpMethod.GET, "/api/v1/schools/{schoolId}/grades")
//				//.access("@schoolSecurity.hasUserId(authentication, #schoolId)")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "NEXTLEVEL_USER", "ORG_ADMIN", "GRADE_ADMIN", "SECTION_ADMIN",
//						"SCHOOL_PRINCIPAL_ADMIN", "SCHOOL_TEACHER_ADMIN", "SCHOOL_FINANCE_ADMIN", "SCHOOL_SPORTS_ADMIN")
//			.antMatchers(HttpMethod.GET, "/api/v1/schools/**")
//				.hasAnyAuthority("NEXTLEVEL_SUPER_ADMIN", "NEXTLEVEL_USER", "ORG_ADMIN", "GRADE_ADMIN", "SECTION_ADMIN",
//						"SCHOOL_PRINCIPAL_ADMIN", "SCHOOL_TEACHER_ADMIN", "SCHOOL_FINANCE_ADMIN", "SCHOOL_SPORTS_ADMIN")
//				//.access("@schoolSecurity.hasUserId(authentication)")
//
//			//grades
//
//			.anyRequest().authenticated()
//			.and()
//			.formLogin().permitAll()
//				.loginPage("/login")
//				.usernameParameter("email")
//				.passwordParameter("pass")
//				.successHandler(databaseLoginSuccessHandler)
//			.and()
//			.oauth2Login()
//				.loginPage("/login")
//				.userInfoEndpoint()
//					.userService(oauth2UserService)
//				.and()
//				.successHandler(oauthLoginSuccessHandler)
//
//			.and()
//			.logout()
//				.invalidateHttpSession(true)
//				.clearAuthentication(true)
//				.deleteCookies("JSESSIONID")
//				.logoutSuccessUrl("/").permitAll()
//
//			.and()
//			.exceptionHandling()
////				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
//				.accessDeniedPage("/403")
//			.and()
//				.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		;
//
//		// Add a filter to validate the tokens with every request
//		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//	}
//
//	@Autowired
//	private CustomOAuth2UserService oauth2UserService;
//
//	@Autowired
//	private OAuthLoginSuccessHandler oauthLoginSuccessHandler;
//
//	@Autowired
//	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
//
//	@Bean
//	public RoleHierarchy roleHierarchy() {
//		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//		String hierarchy = "NEXTLEVEL_SUPER_ADMIN > NEXTLEVEL_USER \n NEXTLEVEL_USER > NGO_GLOBAL_ADMIN";
//		roleHierarchy.setHierarchy(hierarchy);
//		return roleHierarchy;
//	}
//}
