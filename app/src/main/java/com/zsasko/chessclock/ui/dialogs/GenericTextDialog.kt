package com.zsasko.chessclock.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import  com.zsasko.chessclock.R

@Composable
fun GenericTextDialog(
    text: String,
    title: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    stringResource(R.string.general_ok),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
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
        }
    )
}

@Composable
@Preview
fun Preview() {
    GenericTextDialog("Message", "Winner Is Left") {}
}
