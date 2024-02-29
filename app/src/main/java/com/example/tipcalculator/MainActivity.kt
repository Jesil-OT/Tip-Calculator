package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipPercentInput by remember { mutableStateOf("") }
    var numberOfPeople by rememberSaveable { mutableIntStateOf(1) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipPercentInput.toDoubleOrNull() ?: 0.0

    val tip = calculateTip(amount, tipPercent, numberOfPeople)
    val tipTotal = calculateTotal(amount, tipPercent, numberOfPeople)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            value = amountInput,
            onValueChanged = { newValue -> amountInput = newValue },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_money),
                    contentDescription = stringResource(R.string.money_icon_image)
                )
            },
            label = { Text(stringResource(R.string.bill_amount)) }
        )

        EditNumberField(
            value = tipPercentInput,
            onValueChanged = { newValue -> tipPercentInput = newValue },
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.tip_percentage)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_percent),
                    contentDescription = stringResource(R.string.percent_icon)
                )
            }
        )

        AddNumberOfPeopleLayout(
            modifier = Modifier.padding(bottom = 10.dp),
            numberOfPeople = numberOfPeople,
            onSubtractValue = { numberOfPeople-- },
            onAddValue = { numberOfPeople++ }
        )

        Text(
            modifier = Modifier.padding(bottom = 15.dp),
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = stringResource(id = R.string.total_amount, tipTotal),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        leadingIcon = leadingIcon,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun AddNumberOfPeopleLayout(
    modifier: Modifier = Modifier,
    numberOfPeople: Int,
    onSubtractValue: (Int) -> Unit,
    onAddValue: (Int) -> Unit
) {
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = modifier.padding(end = 10.dp),
            onClick = { onSubtractValue(numberOfPeople) },
            enabled = numberOfPeople >= 2
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = null
            )
        }
        Text(
            modifier = modifier.padding(5.dp),
            text = "$numberOfPeople",
            fontSize = 23.sp
        )
        Button(
            modifier = modifier.padding(start = 10.dp),
            onClick = { onAddValue(numberOfPeople) },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
        }
    }
}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
 */
private fun calculateTip(
    amount: Double,
    tipPercent: Double,
    numberOfPeople: Int
): String {
    val tip = tipPercent / 100 * amount
    val tipPerPerson = tip / numberOfPeople
    return NumberFormat.getCurrencyInstance().format(tipPerPerson)
}

private fun calculateTotal(
    amount: Double,
    tipPercent: Double,
    numberOfPeople: Int
) : String {
    val total = tipPercent / 1 + amount / numberOfPeople
    return NumberFormat.getCurrencyInstance().format(total)
}


@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipCalculatorTheme {
        TipTimeLayout()
    }
}

@Preview(showBackground = true)
@Composable
fun AddNumberOfPeopleLayoutPreview() {
    TipCalculatorTheme {
        AddNumberOfPeopleLayout(
            numberOfPeople = 0,
            onSubtractValue = {},
            onAddValue = {}
        )
    }
}