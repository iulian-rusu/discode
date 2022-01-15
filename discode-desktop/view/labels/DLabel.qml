import com.discode.colors 1.0
import com.discode.fonts 1.0

import QtQuick
import QtQuick.Controls

TextEdit {
    id: root

    text: "Placeholder"
    font.family: Font.regular.font.family
    font.pointSize: 16
    color: Colors.staticText

    selectByMouse: true
}
