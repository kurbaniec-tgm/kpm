package pass
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.Document
import pass.salt.SaltApplication
import pass.salt.loader.parser.TOMLObject
import pass.salt.loader.parser.TOMLParser
import java.util.*
import kotlin.test.assertEquals

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = SaltApplication()
        }
    }

}
