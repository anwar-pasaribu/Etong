package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import model.ChatMessage
import service.GenerativeAiService
import ui.component.BackButton
import ui.component.ChatBubbleItem
import ui.component.MessageInput
import viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
class ChatScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chatViewModel: ChatViewModel = remember { ChatViewModel(GenerativeAiService.instance) }
        val chatUiState = chatViewModel.uiState

        val coroutineScope = rememberCoroutineScope()
        val listState = rememberLazyListState()
        Scaffold(
            modifier = Modifier.imePadding(),
            topBar = {
                TopAppBar(
                    title = { Box(Modifier.size(10.dp).background(androidx.compose.ui.graphics.Color.White)) },
                    navigationIcon = {
                        BackButton {
                            navigator.pop()
                        }
                    },
                    actions = {

                    }
                )
            },
            bottomBar = {
                MessageInput(
                    enabled = chatUiState.canSendMessage,
                    onSendMessage = { inputText, image ->
                        chatViewModel.sendMessage(inputText, image)
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                // Messages List
                ChatList(
                    chatMessages = chatUiState.messages,
                    listState = listState,
                )
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                chatViewModel.onCleared()
            }
        }
    }

    @Composable
    fun ChatList(
        chatMessages: List<ChatMessage>,
        listState: LazyListState,
    ) {
        val messages by remember {
            derivedStateOf { chatMessages.reversed() }
        }
        LazyColumn(
            state = listState,
            reverseLayout = true,
        ) {
            items(
                items = messages,
                key = { it.id },
            ) { message ->
                ChatBubbleItem(message)
            }
        }
    }
}
