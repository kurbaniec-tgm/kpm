package pass.dev.server

import org.mindrot.jbcrypt.BCrypt
import pass.dev.db.Password
import pass.dev.db.PasswordRepo
import pass.dev.db.User
import pass.dev.db.UserRepo
import pass.salt.code.annotations.*
import pass.salt.code.annotations.Controller
import pass.salt.code.modules.db.mongo.MongoRepo
import pass.salt.code.modules.server.HTTPTransport
import pass.salt.code.modules.server.security.SessionUser
import pass.salt.code.modules.server.webparse.Model

@Controller
class Controller {
    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var passwordRepo: PasswordRepo


    @Get("/login")
    fun login(m: Model): String {
        return "login"
    }

    @Get("/")
    fun passwdIndex(m: Model, sessionUser: SessionUser): String {
        val user = userRepo.findByUsername(sessionUser.username)
        if (user != null && user.size == 1) {
            val userid = user.first().myid
            val passwords = passwordRepo.findByUserid(userid)
            if (passwords != null) {
                m.addAttribute("entries", passwords.sortedWith(compareBy(Password::organization, Password::name)))
            }
        }
        return "index"
    }

    @Post("/register")
    fun registerUser(@Param("username") username: String, @Param("password") password: String): HTTPTransport {
        val test = userRepo.findByUsername(username)
        return if (test == null) {
            var setPassword = false
            do {
                val nameSpace = ('0'..'9').toList().toTypedArray()
                val myid = (1..101).map { nameSpace.random() }.joinToString("")
                if (userRepo.findByMyid(myid) == null) {
                    userRepo.insert(User(myid, username, BCrypt.hashpw(password, BCrypt.gensalt(12))))
                    setPassword = true
                }
            } while (!setPassword)
            HTTPTransport().ok()
        }
        else HTTPTransport().forbidden()
    }

    @Post("/addentry")
    fun addEntry(@Param("name") name: String, @Param("password") password: String,
                 @Param("link") link: String, @Param("organization") organization: String,
                 sessionUser: SessionUser): HTTPTransport {
        var added = false
        val user = userRepo.findByUsername(sessionUser.username)
        if (user != null && user.size == 1) {
            val userid = user.first().myid
            val test = passwordRepo.findByUseridAndNameAndOrganization(userid, name, organization)
            if (test == null) {
                passwordRepo.insert(Password(userid, name, organization, password, link))
                added = true
            }
        }
        return if (added) HTTPTransport().ok() else HTTPTransport().forbidden()
    }

    @Post("/updatepassword")
    fun updatePassword(@Param("name") name: String, @Param("passwd") password: String,
                       @Param("link") link: String, @Param("org") org: String,
                       sessionUser: SessionUser): HTTPTransport {
        val user = userRepo.findByUsername(sessionUser.username)
        if (user != null && user.size == 1) {
            val userid = user.first().myid
            passwordRepo.updateAll(
                    MongoRepo.Equals(Pair("name", name), Pair("userid", userid), Pair("organization", org)),
                    MongoRepo.Set(Pair("password", password), Pair("link", link)))
            val test = passwordRepo.findByNameAndOrganizationAndPasswordAndLink(name, org, password, link)
            return if (test != null) HTTPTransport().ok() else HTTPTransport().forbidden()
        }
        return HTTPTransport().forbidden()
    }

    @Post("/deletepassword")
    fun deletePassword(@Param("name") name: String, @Param("passwd") password: String,
                       @Param("link") link: String, @Param("org") org: String,
                       sessionUser: SessionUser): HTTPTransport {
        val user = userRepo.findByUsername(sessionUser.username)
        if (user != null && user.size == 1) {
            val userid = user.first().myid
            passwordRepo.deleteAll(MongoRepo.Equals(Pair("name", name), Pair("userid", userid), Pair("organization", org)))
            val test = passwordRepo.findByNameAndOrganizationAndPasswordAndLink(name, org, password, link)
            return if (test == null) HTTPTransport().ok() else HTTPTransport().forbidden()
        }
        return HTTPTransport().forbidden()
    }

    @Get("/account")
    fun account(m: Model, sessionUser: SessionUser): String {
        val user = userRepo.findByUsername(sessionUser.username)
        if (user != null && user.size == 1) {
            m.addAttribute("account", user.first())
        }
        return "account"
    }

    @Post("/updateuserpassword")
    fun updateUserPassword(@Param("name") name: String, @Param("passwordOld") passwordOld: String,
                       @Param("passwordNew") passwordNew: String, sessionUser: SessionUser): HTTPTransport {
        val userCheck = userRepo.findByUsername(sessionUser.username)
        if (userCheck != null && userCheck.size == 1) {
            val userid = userCheck.first().myid
            val user = userRepo.findByMyidAndUsername(userid, name)
            return if (user != null && user.size == 1) {
                val newHashedPassword = BCrypt.hashpw(passwordNew, BCrypt.gensalt(12))
                userRepo.updateAll(
                        MongoRepo.Equals(Pair("username", name), Pair("myid", userid), Pair("password", user.first().password)),
                        MongoRepo.Set(Pair("password", newHashedPassword))
                )
                val lastCheck = userRepo.findByMyidAndUsernameAndPassword(userid, name, newHashedPassword)
                if (lastCheck != null) HTTPTransport().ok() else HTTPTransport().forbidden()
            } else HTTPTransport().failedDependecy()
        }
        return HTTPTransport().failedDependecy()
    }

    @Post("/deleteuser")
    fun deleteUser(@Param("name") name: String, sessionUser: SessionUser): HTTPTransport {
        val userCheck = userRepo.findByUsername(sessionUser.username)
        if (userCheck != null && userCheck.size == 1) {
            val userid = userCheck.first().myid
            userRepo.deleteAll(MongoRepo.Equals(Pair("username", name), Pair("myid", userid)))
            val lastCheck = userRepo.findByMyid(userid)
            return if (lastCheck == null) {
                passwordRepo.deleteAll(MongoRepo.Equals(Pair("userid", userid)))
                sessionUser.endSession()
                HTTPTransport().ok()
            } else HTTPTransport().forbidden()
        }
        return HTTPTransport().forbidden()
    }

}