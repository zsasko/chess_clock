package com.zsasko.chessclock.ui.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import  com.zsasko.chessclock.R

@Composable
fun GenericTextDialog(
    text: String,
    title: String,
    onDismiss: () -> Unit,
) {
    DialogWrapperView(
        title = title,
        content = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )
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
                    onDismiss()
                }
            ) {
                Text(
                    stringResource(R.string.general_ok),
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
