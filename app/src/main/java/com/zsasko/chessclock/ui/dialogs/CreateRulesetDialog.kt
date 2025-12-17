package com.zsasko.chessclock.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zsasko.chessclock.R
import com.zsasko.chessclock.utils.toDigitsOrZero
import com.zsasko.chessclock.viewmodel.ChessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRulesetDialog(
    onDismiss: () -> Unit,
    onApply: (name: String, baseMilliseconds: Long, incrementTimeMs: Long) -> Unit,
    chessViewModel: ChessViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var baseMinutes by remember { mutableIntStateOf(10) }
    var incSeconds by remember { mutableStateOf<Int?>(0) }

    val context = LocalContext.current

    DialogWrapperView(
        title = stringResource(R.string.create_ruleset_dialog_title),
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                OutlinedTextField(
                    label = {
                        Text(text = stringResource(R.string.create_ruleset_dialog_ruleset_name))
                    },
                    value = name,
                    onValueChange = { v -> name = v },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    label = { Text(stringResource(R.string.create_ruleset_dialog_max_play_time)) },
                    value = baseMinutes.toString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { v ->
                        baseMinutes = v.toDigitsOrZero()
                    },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    textStyle = MaterialTheme.typography.bodyLarge,
                    label = { Text(stringResource(R.string.create_ruleset_dialog_time_increment_after_move_made)) },
                    value = (incSeconds ?: "").toString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { v ->
                        incSeconds = if (v.isNotEmpty()) v.toDigitsOrZero() else null
                    }, singleLine = true, modifier = Modifier.fillMaxWidth()
                )

            }
        },
        leftButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    stringResource(R.string.general_dismiss),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        rightButton = {
            TextButton(
                onClick = {
                    val inputValidPair = isInputValid(name, baseMinutes.toLong())
                    if (inputValidPair.first) {
                        chessViewModel.createNewRuleset(
                            name,
                            baseMinutes.minutesToMilliseconds(),
                            (incSeconds ?: 0).secondsToMilliseconds()
                        )
                        onApply(
                            name,
                            baseMinutes.minutesToMilliseconds(),
                            (incSeconds ?: 0).secondsToMilliseconds()
                        )
                    } else {
                        Toast.makeText(context, inputValidPair.second, Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    stringResource(R.string.general_create),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

private fun isInputValid(name: String, baseMinutes: Long): Pair<Boolean, Int> {
    if (name.isEmpty()) {
        return Pair(false, R.string.create_ruleset_dialog_name_required_error)
    }
    if (baseMinutes <= 0) {
        return Pair(false, R.string.create_ruleset_dialog_name_max_play_time_required)
    }
    return Pair(true, -1)
}

fun Int.minutesToMilliseconds(): Long {
    return this * 60_000L
}

fun Int.secondsToMilliseconds(): Long {
    return this * 1_000L
}

@Composable
@Preview
fun PreviewCreateRulesetDialog() {
    CreateRulesetDialog({}, { name, base, increment -> })
}
