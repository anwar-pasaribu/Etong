import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import model.CardLogo
import model.CardUiModel
import ui.TotalBillCardItem
import utils.cardutils.CardType
import kotlin.test.Test

class AppTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest() = runComposeUiTest {
        setContent {
            var text by remember { mutableStateOf("Hello") }
            Text(
                text = text,
                modifier = Modifier.testTag("text")
            )
            Button(
                onClick = { text = "Compose" },
                modifier = Modifier.testTag("button")
            ){
                Text("Click me")
            }
        }

        onNodeWithTag("text").assertTextEquals("Hello")
        onNodeWithTag("button").performClick()
        onNodeWithTag("text").assertTextEquals("Compose")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Total Bill Card Display Show Formatted Number`() = runComposeUiTest {
        setContent {
            val card = CardUiModel(
                cardId = "",
                cardLabel = "",
                cardType = CardType.VISA,
                cardLogo = CardLogo.genericLogo(),
                billAmount = 23_656_000.0,
                billMinAmount = 1_245_000.0,
                billingDate = 0L,
                billDueDate = 0L
            )
            TotalBillCardItem(cardUiModel = card)
        }

        val minAmountNode = onNodeWithTag("test-min-amount")
        minAmountNode.onChildAt(0).assertTextEquals("Rp")
        minAmountNode.onChildAt(1).assertTextEquals("1.245.000")

        val fullAmountNode = onNodeWithTag("test-full-amount")
        fullAmountNode.onChildAt(0).assertTextEquals("Rp")
        fullAmountNode.onChildAt(1).assertTextEquals("23.656.000")
    }
}
