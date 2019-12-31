package pass.dev.server

import org.mindrot.jbcrypt.BCrypt
import pass.dev.db.UserRepo
import pass.salt.code.annotations.Autowired
import pass.salt.code.annotations.WebSecurity
import pass.salt.code.modules.server.security.WebSecurityConfig
import pass.salt.code.modules.server.security.WebSecurityConfigurator

@WebSecurity
class SecTest: WebSecurityConfigurator {

    @Autowired
    lateinit var userRepo: UserRepo

    override fun authenticate(username: String, password: String): Boolean {
        val result = userRepo.findByUsername(username)
        return if (result != null) {
            BCrypt.checkpw(password, result.first().password)
        } else false
    }

    override fun configure(conf: WebSecurityConfig) {
        conf
            .matchRequests("/hello", "/css/login.css", "/favicon.ico", "/register").permitAll()
            .anyRequest().authenticated()
            .loginPath("/login")
            .logoutPath("/logout")
            .successfulLoginPath("/")
    }
}