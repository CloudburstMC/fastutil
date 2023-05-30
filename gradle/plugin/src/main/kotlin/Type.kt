import org.gradle.kotlin.dsl.support.uppercaseFirstChar

class Type(private val type: String, private val parent: Type?) {

    init {
        TYPE_MAP[type] = this
    }

    fun capitalised(): String {
        return type.uppercaseFirstChar()
    }

    fun packagePath(): String {
        if (parent == OBJECT) {
            return parent.packagePath()
        }
        return "$PKG/${plural()}"
    }

    fun plural(): String {
        return type + "s"
    }

    fun parent(): Type? {
        return parent
    }

    override fun toString(): String {
        return type;
    }

    companion object {
        private val TYPE_MAP = mutableMapOf<String, Type>()

        val BOOLEAN = Type("boolean", null)
        val DOUBLE = Type("double", null)
        val FLOAT = Type("float", DOUBLE)
        val INT = Type("int", null)
        val LONG = Type("long", null)
        val OBJECT = Type("object", null)
        val REFERENCE = Type("reference", OBJECT)
        val SHORT = Type("short", INT)
        val BYTE = Type("byte", INT)
        val CHAR = Type("char", INT)

        val TYPES = listOf(BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, OBJECT, REFERENCE, SHORT)

        fun parse(name: String): Type? {
            return TYPE_MAP[name]
        }
    }
}
