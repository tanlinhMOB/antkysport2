/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.material.icons.filled

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.Bill: ImageVector
    get() {
        if (_bill != null) {
            return _bill!!
        }
        _bill = materialIcon(name = "Filled.Bill") {
            materialPath {
                moveTo(6.0f, 2.0f)       // Starting point at top left of bill
                lineToRelative(12.0f, 0.0f) // Horizontal line across top
                lineToRelative(0.0f, 20.0f) // Vertical line down
                lineToRelative(-6.0f, -2.0f) // Bottom right corner
                lineToRelative(-6.0f, 2.0f) // Bottom left corner
                lineTo(6.0f, 2.0f)        // Close the shape

                // Add some lines on the bill as details
                moveTo(8.0f, 6.0f)
                lineToRelative(8.0f, 0.0f) // Top line
                moveTo(8.0f, 10.0f)
                lineToRelative(8.0f, 0.0f) // Middle line
                moveTo(8.0f, 14.0f)
                lineToRelative(4.0f, 0.0f) // Bottom line
            }
        }
        return _bill!!
    }

private var _bill: ImageVector? = null
