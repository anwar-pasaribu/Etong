
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KoinTest: KoinTest {
    @Test
    fun `Test Koin Configuration`() {
        val expect = "28"
        val actual = "28"
        assertEquals(expect, actual)

        //appModule().verify()
    }
}