package com.zsasko.chessclock.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.chessclock.R
import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.utils.DEFAULT_RULESETS

@Composable
fun SelectRulesetDialog(
    initial: ChessRuleset?,
    options: List<ChessRuleset> = DEFAULT_RULESETS,
    onDismiss: () -> Unit,
    onItemSelected: (ChessRuleset) -> Unit
) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    var selectedRuleset by remember { mutableStateOf(initial) }
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.select_ruleset_dialog_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Box {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        isDropDownExpanded.value = true
                    }
                ) {
                    Text(
                        text = selectedRuleset?.name ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier
                    )
                }
                DropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = {
                        isDropDownExpanded.value = false
                    }) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.name, style = MaterialTheme.typography.bodyMedium)
                            },
                            onClick = {
                                isDropDownExpanded.value = false
                                selectedRuleset = option
                            })
                    }
                }
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedRuleset?.let {
                        onItemSelected(it)
                    }
                    onDismiss()
                }
            ) {
                Text(
                    stringResource(R.string.general_confirm),
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

@Preview
@Composable
fun PreviewDialog() {
    SelectRulesetDialog(DEFAULT_RULESETS.first(), DEFAULT_RULESETS, {}, {})
}
