package io.github.fongle.then

val then: Then = TODO()

interface Then {
    operator fun invoke(s: String, block: StringAssertion.() -> Unit)
    operator fun invoke(i: Int, block: Integer.() -> Unit)
    operator fun invoke(b: Boolean, block: BooleanAssertion.() -> Unit)
    operator fun <T> invoke(c: Collection<T>, block: CollectionAssertion<T>.() -> Unit)
    operator fun <K, V> invoke(c: Map<K, V>, block: MapAssert<K, V>.() -> Unit)
}

interface MapAssert<K, V>: Assertion<Map<K, V>> {
    fun keys(block: CollectionAssertion<K>.() -> Unit)
    fun values(block: CollectionAssertion<V>.() -> Unit)
    fun entries(block: CollectionAssertion<Pair<K, V>>.() -> Unit)
    fun contains(vararg expectedEntries: Pair<K, V>) = entries { contains(*expectedEntries) }
    fun contains(vararg expectedKeys: K) = keys { contains(*expectedKeys) }
}

interface StringAssertion: Assertion<String> {
    val isEmpty: Unit
    fun length(block: Integer.() -> Unit): Unit
    override fun isEqualTo(other: String) = isEqualTo(other, ignoreCase = false)
    fun isEqualTo(other: String, ignoreCase: Boolean)
    fun startsWith(prefix: String, ignoreCase: Boolean = false)
    fun endsWith(suffix: String, ignoreCase: Boolean = false)
    fun contains(substring: String, ignoreCase: Boolean = false)
    fun matches(regex: String) = matches(Regex(regex))
    fun matches(regex: Regex)
}

interface BooleanAssertion: Assertion<Boolean> {
    val isTrue get() = isEqualTo(true)
    val isFalse get() = isEqualTo(false)
}

interface CollectionAssertion<T>: Assertion<Collection<T>> {
    val isNotEmpty: Unit
    val isEmpty: Unit
    fun size(block: Integer.() -> Unit)
    fun contains(block: Contains<T>.() -> Unit)
    fun contains(vararg expected: T) = contains { allOf(*expected) }
}

interface Contains<in T> {
    fun allOf(vararg expected: T)
    fun anyOf(vararg expected: T)
    fun noneOf(vararg expected: T)
}

interface Integer: Assertion<Int> {
    infix fun isGreaterThan(minimum: Int): Unit
    fun isLessThan(maximum: Int): Unit
    fun isLessThanOrEqualTo(maximum: Int): Unit
    fun isGreaterThanOrEqualTo(minimum: Int): Unit
}

@ThenDsl
interface Assertion<T> {
    val isNull: Unit
    val isNotNull: Unit
    fun isEqualTo(other: T)
    fun isNotEqualTo(other: T)
    fun isTheSameAs(other: T)
    fun not(block: Assertion<T>.() -> Unit): Assertion<T>
    fun satisfies(block: (T) -> Boolean)
}

@DslMarker
internal annotation class ThenDsl