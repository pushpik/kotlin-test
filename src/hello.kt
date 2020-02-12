import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.types.ObjectId
import javax.activation.MimeType
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Converters
import org.mongodb.morphia.mapping.MappedField
import com.mongodb.BasicDBObject
import org.mongodb.morphia.converters.TypeConverter
import javax.activation.MimeTypeParseException
import java.net.UnknownHostException
import org.mongodb.morphia.Morphia
import java.net.URLEncoder


class myClass {
    private var name: String = "Pushpendra"

    fun printMe() {

        print("name:"+ name)
    }

    class Nested {
        fun surName() {
            print("surname:Sijairya")
        }
    }
}


fun testMongoDB() {
    // write your code here
    val connStr = MongoClientURI("mongodb://hexbid:hex10101@localhost:27017/hbdb");
    val mongoClient = MongoClient(connStr);
    val hbdb = mongoClient.getDatabase("hbdb");
    var collection = hbdb.getCollection("Category");

    var cursor = collection.find().iterator();
    try {
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }
    } finally {
        cursor.close();
    }
}


class MimeTypeConverter : TypeConverter(MimeType::class.java) {

    override fun decode(targetClass: Class<*>, fromDBObject: Any, optionalExtraInfo: MappedField): Any {
        try {
            return MimeType((fromDBObject as BasicDBObject).getString("mimeType"))
        } catch (ex: MimeTypeParseException) {
            return MimeType()
        }

    }

    override fun encode(value: Any, optionalExtraInfo: MappedField?): Any {
        return (value as MimeType).baseType
    }
}


@Entity
@Converters(MimeTypeConverter::class)
class MimeTyped {
    @Id
    lateinit var id: ObjectId;
    lateinit var name: String;
    lateinit var mimeType: MimeType;
}


@Throws(UnknownHostException::class, MimeTypeParseException::class)
fun typeConversionTest() {
    val morphia = Morphia().map(Entity::class.java)
    morphia.mapper.converters.addConverter(MimeTypeConverter())

    val connStr = MongoClientURI("mongodb://admin:${URLEncoder.encode(
        "1timep", "utf-8")}@localhost:27017/admin");
    val mongoClient = MongoClient(connStr);
    val ds = morphia.createDatastore(mongoClient, "test")

    val entity = MimeTyped()
    entity.name = "xml"
    entity.mimeType = MimeType("application/xml")    //MimeTypeParseException

    ds.save(entity);
}


fun main(args: Array<String>) {

    typeConversionTest()

//  Test - 5
//    testMongoDB();

//  Test - 4
//    var slist: MutableList<String>? = mutableListOf()
//    var alist: MutableList<String> = mutableListOf("a", "b")
//    slist!!.addAll(alist)
//
//    println(slist.toString())


    // Test -3
    // val obj = myClass()
    //obj.printMe()

    // Test -2
/*    myClass().printMe()
    myClass.Nested().surName()
    val numbers = intArrayOf(1, 2, 3, 4, 5)
    println("numbers[2]:" + numbers[2])
*/

    // Test -1
/*  println("Hello World!!")

    val a: Int = 5
    val s: String = "pushpendra\nSijairya"

    println("int="+ a)
    println("String="+s)


    val numbers: IntArray = intArrayOf(1, 2, 3, 4, 5)
    println("numbers[2]:" + numbers[2])


    val numbers1: MutableList<Int> = mutableListOf(1, 2, 3)
    val readOnlyView: List<Int> = numbers1
    println("mutable list" + numbers1)
    numbers1.add(4)
    println("mutable list after addition"+ numbers1)

    println("readOnlyView" + readOnlyView)
    readOnlyView.clear()
*/
}