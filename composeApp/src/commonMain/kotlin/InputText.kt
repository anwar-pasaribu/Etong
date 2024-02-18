import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import ui.priceFilter

@Composable
fun InputNumber(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    label: String,
    placeholder: String,
    lengthLimit: Int = 10,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next
    ),
    onTyped: (String) -> Unit,
) {
    val pattern = remember { Regex("^\\d+\$") }
    var typedNumber by remember { mutableStateOf("") }
    if (initialValue.isNotEmpty()) {
        typedNumber = initialValue
    }
    OutlinedTextField(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        value = typedNumber,
        onValueChange = { str ->
            if (str.isEmpty()) {
                typedNumber = str
                onTyped(str)
            } else if (str.length <= lengthLimit && str.matches(pattern)) {
                typedNumber = str
                onTyped(str)
            }
        },
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        visualTransformation = {
            priceFilter(it.text)
        },
    )
}