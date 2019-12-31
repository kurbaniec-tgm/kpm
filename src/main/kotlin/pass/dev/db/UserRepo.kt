package pass.dev.db

import pass.salt.code.annotations.MongoDB
import pass.salt.code.modules.db.mongo.MongoRepo

@MongoDB
interface UserRepo: MongoRepo<User, String> {

    fun findByUsername(username: String): List<User>?

    fun findByUsernameAndPassword(username: String, password: String): List<User>?

    fun findByMyidAndUsername(myid: String, username: String): List<User>?

    fun findByMyidAndUsernameAndPassword(myid: String, username: String, password: String): List<User>?

    fun findByMyid(myid: String): List<User>?

}