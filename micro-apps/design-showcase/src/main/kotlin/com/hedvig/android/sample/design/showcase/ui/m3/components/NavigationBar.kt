/**
 * MIT License
 *
 * Copyright (c) 2023 Odin Asbjørnsen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.hedvig.android.sample.design.showcase.ui.m3.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun M3NavigationBars() {
  val navbarItems = listOf("Home", "Payments")
  var selectedItem by remember { mutableIntStateOf(0) }
  Column {
    Spacer(Modifier.size(16.dp))
    M3OnSurfaceText(
      text = "Bottom app bars",
      style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(Modifier.size(16.dp))
    Column {
      NavigationBar {
        navbarItems.forEachIndexed { index, title ->
          NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(title) },
            selected = selectedItem == index,
            onClick = { selectedItem = index },
          )
        }
      }
      Spacer(Modifier.size(16.dp))
      Column {
        NavigationBar {
          navbarItems.forEachIndexed { index, title ->
            NavigationBarItem(
              icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
              label = { Text(title) },
              selected = selectedItem == index,
              onClick = { selectedItem = index },
              alwaysShowLabel = false,
            )
          }
        }
      }
    }
  }
}
