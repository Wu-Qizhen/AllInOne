package com.wqz.allinone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wqz.allinone.R
import com.wqz.allinone.ui.theme.AllInOneTheme

object ConfirmationDialog {
    @Composable
    fun DeleteConfirmationDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            TitleBar.TextTitleBar(title = R.string.text_delete_confirm)
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.text_delete_confirm_hint),
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                ItemX.Button(text = stringResource(R.string.delete), onConfirm)
                Spacer(modifier = Modifier.width(10.dp))
                ItemX.Button(text = stringResource(R.string.cancel), onDismiss)
            }
        }
    }
}

@Preview
@Composable
fun DeleteConfirmationDialogPreview() {
    AllInOneTheme {
        AppBackground.CirclesBackground {
            ConfirmationDialog.DeleteConfirmationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}