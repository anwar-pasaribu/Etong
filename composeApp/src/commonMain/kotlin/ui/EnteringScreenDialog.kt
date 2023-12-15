package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EnteringScreen(onDismissRequest: () -> Unit, onSubmitRequest: (String, String) -> Unit) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var userIdentification by remember { mutableStateOf("") }
                var userAuthorization by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userIdentification, onValueChange = {
                        userIdentification = it
                    },
                    placeholder = {
                        Text(text = "Email")
                    },
                    label = {
                        Text(text = "Email")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userAuthorization,
                    onValueChange = {
                        userAuthorization = it
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text(text = "Password") },
                    label = { Text(text = "Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )

                Spacer(modifier = Modifier.height(16.dp))
                val buttonEnabled = userIdentification.isNotEmpty()
                        && userAuthorization.isNotEmpty()
                Button(onClick = {
                    onSubmitRequest(
                        userIdentification, userAuthorization
                    )
                    onDismissRequest()
                }, enabled = buttonEnabled ) {
                    Text(text = "Login/Register")
                }
            }
        }
    }
}